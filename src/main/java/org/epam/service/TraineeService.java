package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.epam.exceptions.VerificationException;
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
        Trainee trainee = gymDao.create(prepare(request));
        log.info("Created " + getModelName() + " with id " + trainee.getId());
        return traineeMapper.traineeToRegistrationResponse(trainee);
    }

    @Transactional
    public TraineeProfileResponse update(UpdateTraineeProfileRequest request) throws VerificationException {
        User user = userDao.getByUsername(request.getUsername());
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstname());
        user.setLastName(request.getLastname());
        user.setActive(request.isActive());
        Trainee trainee = gymDao.getModelByUser(userDao.update(user.getId(), user));
        if (request.getDateOfBirth() != null) trainee.setDateOfBirth(request.getDateOfBirth());
        if (request.getAddress() != null) trainee.setAddress(request.getAddress());
        trainee.setUser(user);
        trainee = gymDao.update(trainee.getId(), trainee);
        return traineeMapper.traineeToProfileResponse(trainee);
    }
    @Transactional(readOnly = true)
    public TraineeProfileResponse selectByUsername(String username) throws VerificationException {
        User user = userDao.getByUsername(username);
        Trainee trainee = gymDao.getModelByUser(user);
        if (trainee == null) throw new ProhibitedActionException("No one except Trainee could not use TraineeService");
        return traineeMapper.traineeToProfileResponse(trainee);
    }
    @Transactional
    public boolean changePassword(ChangeLoginRequest request) throws VerificationException {
        User user;
        try {
            user = userDao.getByUsername(request.getUsername());
        } catch (InvalidDataException e) {
            return false;
        }
        Trainee trainee = gymDao.getModelByUser(user);
        if (trainee == null)
            throw new ProhibitedActionException("No one except Trainee could not use TraineeService");
        if (user.getPassword().equals(request.getNewPassword())) throw new ProhibitedActionException("New password is the same as old");
        user.setPassword(request.getNewPassword());
        return userDao.update(user.getId(), user).getPassword().equals(request.getNewPassword());
    }

    @Transactional
    public boolean setActive(ActivateDeactivateRequest request) throws VerificationException {
        User user = userDao.getByUsername(request.getUsername());
        Trainee trainee = gymDao.getModelByUser(user);
        if (trainee == null) throw new ProhibitedActionException("No one except Trainee could not use TraineeService");
        if (user.isActive() != request.isActive()) userDao.update(user.getId(), user);
        return true;
    }

    @Transactional
    public boolean delete(String username) throws VerificationException {
        User user = userDao.getByUsername(username);
        Trainee trainee = gymDao.getModelByUser(user);
        if (trainee == null) throw new ProhibitedActionException("No one except Trainee could not use TraineeService");
        gymDao.delete(trainee.getId());
        return userDao.delete(user.getId()).equals(user);
    }

    private Trainee prepare(TraineeRegistrationRequest request) {
        log.info("Creating " + getModelName());
        Trainee trainee = new Trainee();
        User user = userDao.setNewUser(request.getFirstname(), request.getLastname());
        log.info("Creating " + getModelName() + " with user " + request.getFirstname() + " " + request.getLastname());
        trainee.setUser(user);
        trainee.setAddress(request.getAddress());
        trainee.setDateOfBirth(request.getDateOfBirth());
        log.info("Created " + getModelName() + "and parametrized");
        return trainee;
    }

    protected String getModelName() {
        return "Trainee";
    }
}
