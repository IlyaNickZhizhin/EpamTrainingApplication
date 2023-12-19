package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.epam.dao.TraineeRepository;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeProfileResponse;
import org.epam.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.dto.traineeDto.UpdateTraineeProfileRequest;
import org.epam.dto.trainingDto.GetTraineeTrainingsListRequest;
import org.epam.dto.trainingDto.GetTrainingsResponse;
import org.epam.exceptions.InvalidDataException;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.mapper.TraineeMapper;
import org.epam.mapper.TrainingMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TraineeService {

    private final TraineeMapper traineeMapper;
    private final TrainingMapper trainingMapper;
    private final TraineeRepository traineeRepository;
    private final UserService userService;

    @Transactional
    public RegistrationResponse create(TraineeRegistrationRequest request) {
        log.info("Creating " + getModelName());
        Trainee trainee = prepare(request);
        return traineeMapper.traineeToRegistrationResponse(traineeRepository.save(trainee));
    }

    @Transactional
    public TraineeProfileResponse update(String username, UpdateTraineeProfileRequest request) {
        ImmutablePair<User, Trainee> pair = getUserTrainee(username);
        pair.left.setUsername(request.getUsername());
        pair.left.setFirstName(request.getFirstname());
        pair.left.setLastName(request.getLastname());
        pair.left.setActive(request.isActive());
        if (request.getDateOfBirth() != null) pair.right.setDateOfBirth(request.getDateOfBirth());
        if (request.getAddress() != null) pair.right.setAddress(request.getAddress());
        pair.right.setUser(userService.update(pair.left.getId(), pair.left).orElseThrow(() -> {
            log.error("Troubles with updating user " + request.getUsername());
            return new InvalidDataException("userDao.update(" + pair.left.getId() + ", " + pair.left + ")",
                    "Troubles with updating user " + request.getUsername());
        }));
        Trainee updateTrainee = traineeRepository.save(pair.right);
        return traineeMapper.traineeToProfileResponse(updateTrainee);
    }
    @Transactional(readOnly = true)
    public TraineeProfileResponse selectByUsername(String username) {
        Trainee trainee = getUserTrainee(username).right;
        return traineeMapper.traineeToProfileResponse(trainee);
    }
    @Transactional
    public boolean changePassword(ChangeLoginRequest request) {
        ImmutablePair<User,Trainee> pair = getUserTrainee(request.getUsername());
        if (pair.left.getPassword().equals(request.getNewPassword())) {
            throw new ProhibitedActionException("New password is the same as old");
        }
        pair.left.setPassword(request.getNewPassword());
        return userService.update(pair.left.getId(), pair.left).orElseThrow(() -> {
            log.error("Troubles with updating user " + request.getUsername());
            return new InvalidDataException("userDao.update(" + pair.left.getId() + ", " + pair.left + ")",
                    "Troubles with updating user " + request.getUsername());
        }).getPassword().equals(request.getNewPassword());
    }

    @Transactional
    public boolean setActive(ActivateDeactivateRequest request) {
        User user = getUserTrainee(request.getUsername()).left;
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
    public GetTrainingsResponse getTraineeTrainingsList(String username, GetTraineeTrainingsListRequest request) {
        Trainee trainee = getTrainee(username);
        List<Training> trainings = trainingFilterByDate(trainee.getTrainings(), request.getPeriodFrom(), request.getPeriodTo());
        if (request.getTrainingType() != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainingType().equals(TrainingType.of(request.getTrainingType())))
                    .collect(Collectors.toList());
        }
        if (request.getTrainerName() != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainer().getUser().getUsername().equals(request.getTrainerName()))
                    .collect(Collectors.toList());
        }
        GetTrainingsResponse response = new GetTrainingsResponse();
        response.setTrainings(trainingMapper.traineeTrainingsToShortDtos(trainings));
        return response;
    }

    @Transactional
    public boolean delete(String username) {
        ImmutablePair<User,Trainee> pair = getUserTrainee(username);
        traineeRepository.deleteById(pair.right.getId());
        return userService.delete(pair.left.getId()).orElse(new User()).equals(pair.left);
    }

    private Trainee prepare(TraineeRegistrationRequest request) {
        log.info("Creating " + getModelName());
        Trainee trainee = new Trainee();
        User user = userService.setNewUser(request.getFirstname(), request.getLastname()).orElseThrow(() -> {
            log.error("Troubles with creating user: " + request.getFirstname() + "." + request.getLastname());
            return new InvalidDataException("userDao.setNewUser(" + request.getFirstname() + ", " + request.getLastname() + ")"
                    , "Troubles with creating user: " + request.getFirstname() + "." + request.getLastname());
        });
        log.info("Creating " + getModelName() + " with user: " + request.getFirstname() + "." + request.getLastname());
        trainee.setUser(user);
        trainee.setAddress(request.getAddress());
        trainee.setDateOfBirth(request.getDateOfBirth());
        log.info("Created " + getModelName() + "and parametrized");
        return trainee;
    }

    protected String getModelName() {
        return "Trainee";
    }

    private ImmutablePair<User,Trainee> getUserTrainee(String username) {
        User user = userService.findByUsername(username).orElseThrow(() -> {
            log.error("No user with username: " + username);
            return new InvalidDataException("userDao.getByUsername(" + username + ")", "No user with username: " + username);
        });
        Trainee trainee = traineeRepository.findByUser(user).orElseThrow(() -> {
            log.error("No trainee with username: " + username);
            return new ProhibitedActionException("No one except trainee could use this service, " +
                    "but there are no trainee with username: " + username);
        });
        return new ImmutablePair<>(user,trainee);
    }

    private Trainee getTrainee(String username) {
        User user = userService.findByUsername(username).orElseThrow(() -> {
            log.error("No user with username: " + username);
            return new InvalidDataException("userDao.getByUsername(" + username + ")", "No user with username: " + username);
        });
        return traineeRepository.findByUser(user).orElseThrow(() -> {
            log.error("No trainee with username " + username);
            return new ProhibitedActionException("No one except trainee could use this method in trainingService, " +
                    "but there are no trainee with username: " + username);
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


