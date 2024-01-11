package org.epam.gymservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.epam.gymservice.dto.ActivateDeactivateRequest;
import org.epam.gymservice.dto.ChangeLoginRequest;
import org.epam.gymservice.dto.RegistrationResponse;
import org.epam.gymservice.dto.traineeDto.TraineeProfileResponse;
import org.epam.gymservice.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.gymservice.dto.traineeDto.UpdateTraineeProfileRequest;
import org.epam.gymservice.dto.trainingDto.GetTraineeTrainingsListRequest;
import org.epam.gymservice.dto.trainingDto.GetTrainingsResponse;
import org.epam.gymservice.exceptions.InvalidDataException;
import org.epam.gymservice.exceptions.ProhibitedActionException;
import org.epam.gymservice.mapper.TraineeMapper;
import org.epam.gymservice.mapper.TrainingMapper;
import org.epam.gymservice.model.Role;
import org.epam.gymservice.model.User;
import org.epam.gymservice.model.gymModel.Trainee;
import org.epam.gymservice.model.gymModel.Training;
import org.epam.gymservice.model.gymModel.TrainingType;
import org.epam.gymservice.repository.TraineeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TraineeService {

    private final TraineeMapper traineeMapper;
    private final TrainingMapper trainingMapper;
    private final TraineeRepository traineeRepository;
    private final UserService userService;
    private final PasswordEncoder encoder;

    @Transactional
    public RegistrationResponse create(TraineeRegistrationRequest request) {
        log.info("Creating " + getModelName());
        ImmutablePair<Trainee, String> traineeWithPass = prepare(request);
        Trainee trainee = traineeWithPass.left;
        log.info("Created " + getModelName() + " with id " + trainee.getId());
        traineeRepository.save(trainee);
        return new RegistrationResponse(trainee.getUser().getUsername(), traineeWithPass.right);
    }

    @Transactional
    public TraineeProfileResponse update(UpdateTraineeProfileRequest request) {
        ImmutablePair<User, Trainee> pair = getUserTrainee(request.getUsername());
        pair.left.setUsername(request.getUsername());
        pair.left.setFirstName(request.getFirstname());
        pair.left.setLastName(request.getLastname());
        pair.left.setActive(request.isActive());
        if (request.getDateOfBirth() != null) pair.right.setDateOfBirth(request.getDateOfBirth());
        if (request.getAddress() != null) pair.right.setAddress(request.getAddress());
        log.info("User #" + pair.left.getId() + "and Trainee #" + pair.right.getId() 
                + "updated successfully, going to save models");
        pair.right.setUser(userService.update(pair.left.getId(), pair.left).orElseThrow(() -> {
            log.error("Troubles with updating trainee #: " + pair.right.getId() 
                    + "with user #:" + pair.left.getId());
            return new InvalidDataException("userDao.update(" + pair.left.getId() + ", the user)",
                    "Troubles with updating user #" + pair.left.getId());
        }));
        log.info("User #" + pair.left.getId() + "updates save successfully, going to save trainee");
        Trainee updateTrainee = traineeRepository.save(pair.right);
        log.info("Trainee #" + updateTrainee.getId() + "saved successfully");
        return traineeMapper.traineeToProfileResponse(updateTrainee);
    }
    @Transactional(readOnly = true)
    public TraineeProfileResponse selectByUsername(String username) {
        Trainee trainee = getTrainee(username);
        log.info("Trainee #" + trainee.getId() + "found successfully");
        return traineeMapper.traineeToProfileResponse(trainee);
    }
    @Transactional
    public boolean changePassword(ChangeLoginRequest request) {
        ImmutablePair<User,Trainee> pair = getUserTrainee(request.getUsername());
        if (encoder.matches(request.getNewPassword(), pair.left.getPassword())) {
            throw new ProhibitedActionException("New password is the same as old");
        }
        pair.left.setPassword(encoder.encode(request.getNewPassword()));
        return encoder.matches(request.getNewPassword(), userService.update(pair.left.getId(), pair.left)
                .orElseThrow(
                        () -> {
                            log.error("Troubles with updating user #" + pair.left.getId());
                            return new InvalidDataException("userDao.update(" + pair.left.getId() + ", the user)",
                                    "Troubles with updating user #" + pair.left.getId());
                        }
                )
                .getPassword());
    }

    @Transactional
    public boolean setActive(ActivateDeactivateRequest request) {
        User user = getUserTrainee(request.getUsername()).left;
        if (user.isActive() != request.isActive()) {
            user.setActive(request.isActive());
            userService.update(user.getId(), user).orElseThrow(() -> {
                log.error("Troubles with updating user #" + user.getId());
                return new InvalidDataException("userDao.update(" + user.getId() + ", the user)",
                        "Troubles with updating user #" + user.getId());
            });
        }
        return true;
    }

    @Transactional(readOnly = true)
    public GetTrainingsResponse getTraineeTrainingsList(String username, GetTraineeTrainingsListRequest request) {
        log.info("Getting trainee trainings list in" + getClass().getSimpleName());
        Trainee trainee = getTrainee(username);
        List<Training> trainings = trainingFilterByDate(trainee.getTrainings(), request.getPeriodFrom(), request.getPeriodTo());
        log.info("Trainee #" +trainee.getId()+ "trainings list with size:" + trainings.size() + "received successfully");
        if (request.getTrainingType() != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainingType().equals(TrainingType.of(request.getTrainingType())))
                    .collect(Collectors.toList());
            log.info("Trainee trainings list filtered by type" + request.getTrainingType().name() 
                    + "to size:" + trainings.size());
        }
        if (request.getTrainerName() != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainer().getUser().getUsername().equals(request.getTrainerName()))
                    .collect(Collectors.toList());
            log.info("Trainee trainings list filtered by trainer" + request.getTrainerName() 
                    + "to size:" + trainings.size());
        }
        GetTrainingsResponse response = new GetTrainingsResponse();
        response.setTrainings(trainingMapper.traineeTrainingsToShortDtos(trainings));
        log.info("Trainee #" +trainee.getId()+ "trainings list filtered by all parameters to size:" + trainings.size());
        return response;
    }

    @Transactional
    public boolean delete(String username) {
        ImmutablePair<User,Trainee> pair = getUserTrainee(username);
        log.info("Deleting " + getModelName() + "# " + pair.right.getId() + " with user # " + pair.left.getId());
        traineeRepository.deleteById(pair.right.getId());
        log.info("Deleting " + getModelName() + "# " + pair.right.getId() + " was successful." +
                " Going ro delete user #" + pair.left.getId());
        return userService.delete(pair.left.getId()).orElse(new User()).equals(pair.left);
    }

    private ImmutablePair<Trainee, String> prepare(TraineeRegistrationRequest request) {
        log.info("Creating " + getModelName());
        Trainee trainee = new Trainee();
        ImmutablePair<Optional<User>, String> userWithPass =
                userService.setNewUser(request.getFirstname(), request.getLastname(), Role.of(Role.Authority.ROLE_TRAINEE));
        User user = userWithPass.left.orElseThrow(() -> {
            log.error("Troubles with creating user: " + request.getFirstname().substring(0,0) + "." 
                    + request.getLastname().substring(0,0));
            return new InvalidDataException("userDao.setNewUser(" + request.getFirstname().substring(0,0) + "***, "
                    + request.getLastname().substring(0,0) + "***)"
                    , "Troubles with creating user: " + request.getFirstname().substring(0,0) + "***."
                    + request.getLastname().substring(0,0));
        });
        log.info("User created with id:" + user.getId() + " going to parametrize " + getModelName());
        trainee.setUser(user);
        trainee.setAddress(request.getAddress());
        trainee.setDateOfBirth(request.getDateOfBirth());
        log.info("Created " + getModelName() + "and parametrized");
        return ImmutablePair.of(trainee, userWithPass.right);
    }

    protected String getModelName() {
        return "Trainee";
    }

    private ImmutablePair<User,Trainee> getUserTrainee(String username) {
        User user = userService.findByUsername(username).orElseThrow(() -> {
            log.error("No user with username: " + username);
            return new InvalidDataException("userDao.getByUsername(" + username + ")", "No user with username: " + username);
        });
        log.info("User with id: " + user.getId() + " found");
        Trainee trainee = traineeRepository.findByUser(user).orElseThrow(() -> {
            log.error("No trainee with user # " + user.getId());
            return new ProhibitedActionException("No one except trainee could use this service, " +
                    "but there are no trainee with user #: " + user.getId());
        });
        log.info("Trainee with id: " + trainee.getId() + " found");
        return new ImmutablePair<>(user,trainee);
    }

    private Trainee getTrainee(String username) {
        User user = userService.findByUsername(username).orElseThrow(() -> {
            log.error("No user with username: " + username);
            return new InvalidDataException("userDao.getByUsername(" + username + ")", "No user with username: " + username);
        });
        log.info("User with id: " + user.getId() + " found");
        return traineeRepository.findByUser(user).orElseThrow(() -> {
            log.error("No trainee with user #" + user.getId());
            return new ProhibitedActionException("No one except trainee could use this method in trainingService, " +
                    "but there are no trainee with user #: " + user.getId());
        });
    }

    private List<Training> trainingFilterByDate(List<Training> trainings, LocalDate periodFrom, LocalDate periodTo) {
        if (periodFrom != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainingDate().isAfter(periodFrom))
                    .collect(Collectors.toList());
            log.info("Trainee trainings list filtered by date from " + periodFrom
                    + "to size:" + trainings.size());
        }
        if (periodTo != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainingDate().isBefore(periodTo))
                    .collect(Collectors.toList());
            log.info("Trainee trainings list filtered by date to " + periodTo
                    + "to size:" + trainings.size());
        }
        return trainings;
    }
}


