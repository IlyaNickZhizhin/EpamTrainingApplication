package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.epam.dao.TraineeDaoImpl;
import org.epam.dao.UserDao;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeProfileResponse;
import org.epam.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.dto.traineeDto.UpdateTraineeProfileRequest;
import org.epam.exceptions.InvalidDataException;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.mapper.TraineeMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TraineeService {

    private final TraineeMapper traineeMapper;
    private final TraineeDaoImpl gymDao;
    private final UserDao userDao;

    @Transactional
    public RegistrationResponse create(TraineeRegistrationRequest request) {
        log.info("Creating " + getModelName());
        Trainee trainee = gymDao.create(prepare(request)).orElseThrow(() -> {
            log.error("Troubles with creating " + getModelName() + " with user: " + request.getFirstname() + "." + request.getLastname());
            throw new InvalidDataException("gymDao.create(" + request + ")" ,
                    "Troubles with creating " + getModelName() + " with user: " + request.getFirstname() + "." + request.getLastname());
        });
        log.info("Created " + getModelName() + " with id " + trainee.getId());
        return traineeMapper.traineeToRegistrationResponse(trainee);
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
        pair.right.setUser(userDao.update(pair.left.getId(), pair.left).orElseThrow(() -> {
            log.error("Troubles with updating user " + request.getUsername());
            return new InvalidDataException("userDao.update(" + pair.left.getId() + ", " + pair.left + ")",
                    "Troubles with updating user " + request.getUsername());
        }));
        Trainee updateTrainee = gymDao.update(pair.right.getId(), pair.right).orElseThrow(
                () -> {
                    log.error("Troubles with updating " + getModelName() + " " + request.getUsername());
                    return new InvalidDataException("gymDao.update(" + pair.right.getId() + ", " + pair.right + ")",
                            "Troubles with updating " + getModelName() + " " + request.getUsername());
                }
        );
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
        return userDao.update(pair.left.getId(), pair.left).orElseThrow(() -> {
            log.error("Troubles with updating user " + request.getUsername());
            return new InvalidDataException("userDao.update(" + pair.left.getId() + ", " + pair.left + ")",
                    "Troubles with updating user " + request.getUsername());
        }).getPassword().equals(request.getNewPassword());
    }

    @Transactional
    public boolean setActive(ActivateDeactivateRequest request) {
        User user = getUserTrainee(request.getUsername()).left;
        if (user.isActive() != request.isActive()) userDao.update(user.getId(), user).orElseThrow(() -> {
            log.error("Troubles with updating user " + request.getUsername());
            return new InvalidDataException("userDao.update(" + user.getId() + ", " + user + ")",
                    "Troubles with updating user " + request.getUsername());
        });
        return true;
    }

    @Transactional
    public boolean delete(String username) {
        ImmutablePair<User,Trainee> pair = getUserTrainee(username);
        gymDao.delete(pair.right.getId());
        return userDao.delete(pair.left.getId()).orElse(new User()).equals(pair.left);
    }

    private Trainee prepare(TraineeRegistrationRequest request) {
        log.info("Creating " + getModelName());
        Trainee trainee = new Trainee();
        User user = userDao.setNewUser(request.getFirstname(), request.getLastname()).orElseThrow(() -> {
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
        User user = userDao.getByUsername(username).orElseThrow(() -> {
            log.error("No user with username: " + username);
            return new InvalidDataException("userDao.getByUsername(" + username + ")", "No user with username: " + username);
        });
        Trainee trainee = gymDao.getModelByUser(user).orElseThrow(() -> {
            log.error("No trainee with username: " + username);
            return new ProhibitedActionException("No one except trainee could use this service, " +
                    "but there are no trainee with username: " + username);
        });
        return new ImmutablePair<>(user,trainee);
    }
}
