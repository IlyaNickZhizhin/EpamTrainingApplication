package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.epam.dao.TrainerRepository;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.trainerDto.TrainerProfileResponse;
import org.epam.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.dto.trainerDto.UpdateTrainerProfileRequest;
import org.epam.dto.trainingDto.GetTrainerTrainingsListRequest;
import org.epam.dto.trainingDto.GetTrainingsResponse;
import org.epam.exceptions.InvalidDataException;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.mapper.TrainerMapper;
import org.epam.mapper.TrainingMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final UserService userService;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;
    @Transactional
    public RegistrationResponse create(TrainerRegistrationRequest request) {
        log.info("Creating " + getModelName());
        User user = userService.setNewUser(request.getFirstName(), request.getLastName()).orElseThrow(() -> {
            log.error("Troubles with creating user: " + request.getFirstName() + "." + request.getLastName());
            return new InvalidDataException("userDao.setNewUser(" + request.getFirstName() + ", " + request.getLastName() + ")",
                    "Troubles with creating user: " + request.getFirstName() + " " + request.getLastName());
        });
        log.info("Creating " + getModelName() + " with user: " + request.getFirstName() + "." + request.getLastName());
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(trainerMapper.stringToTrainingType(request.getSpecialization()));
        log.info("Created " + getModelName() + " with id " + trainer.getId());
        return trainerMapper.trainerToRegistrationResponse(trainerRepository.save(trainer));

    }
    @Transactional
    public TrainerProfileResponse update(String username, UpdateTrainerProfileRequest request) {
        ImmutablePair<User, Trainer> pair = getUserTrainer(username);
        pair.left.setUsername(request.getUsername());
        pair.left.setFirstName(request.getFirstName());
        pair.left.setLastName(request.getLastName());
        pair.left.setActive(request.isActive());
        if (request.getSpecialization() != null)
            pair.right.setSpecialization(trainerMapper.stringToTrainingType(request.getSpecialization()));
        pair.right.setUser(userService.update(pair.left.getId(), pair.left)
                .orElseThrow(() -> {
                    log.error("Troubles with updating user " + request.getUsername());
                    return new InvalidDataException("userDao.update(" + pair.left.getId() + ", " + pair.left + ")",
                            "Troubles with updating user " + request.getUsername());
                }));
        return trainerMapper.trainerToProfileResponse(trainerRepository.save(pair.right));
    }
    @Transactional(readOnly = true)
    public TrainerProfileResponse selectByUsername(String username) {
        return trainerMapper.trainerToProfileResponse(getUserTrainer(username).right);
    }

    @Transactional
    public boolean changePassword(ChangeLoginRequest request) {
        ImmutablePair<User, Trainer> pair = getUserTrainer(request.getUsername());
        if (pair.left.getPassword().equals(request.getNewPassword())) throw new ProhibitedActionException("New password is the same as old");
        pair.left.setPassword(request.getNewPassword());
        return userService.update(pair.left.getId(), pair.left)
                .orElseThrow(
                        () -> {
                            log.error("Troubles with updating user " + request.getUsername());
                            return new InvalidDataException("userDao.update(" + pair.left.getId() + ", " + pair.left + ")",
                                    "Troubles with updating user " + request.getUsername());
                        }
                )
                .getPassword().equals(request.getNewPassword());
    }
    @Transactional
    public boolean setActive(ActivateDeactivateRequest request) {
        User user = getUserTrainer(request.getUsername()).left;
        if (user.isActive() != request.isActive()) {
            user.setActive(request.isActive());
            userService.update(user.getId(), user).orElseThrow(() -> {
                log.error("Troubles with updating user " + request.getUsername());
                return new InvalidDataException("userDao.update(" + user.getId() + ", " + user + ")",
                        "Troubles with updating user " + request.getUsername());
            });
        }
        return true;
    }

    @Transactional(readOnly = true)
    public GetTrainingsResponse getTrainerTrainingsList(String username, GetTrainerTrainingsListRequest request) {
        Trainer trainer = getTrainer(username);
        List<Training> trainings = trainingFilterByDate(trainer.getTrainings(), request.getPeriodFrom(), request.getPeriodTo());
        if (request.getTraineeName() != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainee().getUser().getUsername().equals(request.getTraineeName()))
                    .collect(Collectors.toList());
        }
        GetTrainingsResponse response = new GetTrainingsResponse();
        response.setTrainings(trainingMapper.trainerTrainingsToShortDtos(trainings));
        return response;
    }


    protected String getModelName() {
        return "Trainer";
    }

    private ImmutablePair<User, Trainer> getUserTrainer(String username) {
        User user = userService.findByUsername(username).orElseThrow(() -> {
            log.error("No user with username " + username);
            return new InvalidDataException("userDao.getByUsername(" + username + ")", "No user with username " + username);
        });
        Trainer trainer = trainerRepository.findByUser(user).orElseThrow(() -> {
            log.error("No trainee with username " + username);
            return new ProhibitedActionException("No one except trainer could use this service, " +
                    "but there are no trainer with username " + username);
        });
        return new ImmutablePair<>(user,trainer);
    }

    private Trainer getTrainer(String username) {
        User user = userService.findByUsername(username).orElseThrow(() -> {
            log.error("No user with username " + username);
            return new InvalidDataException("userDao.getByUsername(" + username + ")", "No user with username: " + username);
        });
        return trainerRepository.findByUser(user).orElseThrow(() -> {
            log.error("No trainee with username: " + username);
            return new ProhibitedActionException("No one except trainer could use this method in trainingService, " +
                    "but there are no trainer with username: " + username);
        });
    }

    private List<Training> trainingFilterByDate(List<Training> trainings, LocalDate periodFrom, LocalDate periodTo) {
        if (periodFrom != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainingDate().isAfter(periodFrom))
                    .collect(Collectors.toList());
        }
        if (periodTo != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainingDate().isBefore(periodTo))
                    .collect(Collectors.toList());
        }
        return trainings;
    }

}
