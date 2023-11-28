package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.dao.TraineeDaoImpl;
import org.epam.dao.UserDaoImpl;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.LoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeProfileResponse;
import org.epam.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.dto.traineeDto.UpdateTraineeProfileRequest;
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
    private final UserDaoImpl userDao;
    private final GymGeneralService<Trainee> superService;

    @Transactional
    public RegistrationResponse create(TraineeRegistrationRequest request) {
        log.info("Creating " + getModelName());
        Trainee trainee = gymDao.create(prepare(request));
        log.info("Created " + getModelName() + " with id " + trainee.getId());
        return traineeMapper.traineeToRegistrationResponse(trainee);
    }

    @Transactional
    public TraineeProfileResponse update(UpdateTraineeProfileRequest request) throws VerificationException {
        Trainee trainee = superService.selectByUsername(request.getUsername());
        User user = trainee.getUser();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstname());
        user.setLastName(request.getLastname());
        user.setActive(request.isActive());
        trainee.setUser(user);
        if (request.getDateOfBirth() != null) trainee.setDateOfBirth(request.getDateOfBirth());
        if (request.getAddress() != null) trainee.setAddress(request.getAddress());
        trainee = gymDao.update(trainee.getId(), trainee);
        return traineeMapper.traineeToProfileResponse(trainee);
    }

    @Transactional
    public void changePassword(ChangeLoginRequest request) throws VerificationException {
        Trainee trainee = superService.selectByUsername(request.getUsername());
        if (trainee==null) throw new ProhibitedActionException("No one except Trainee could not use TraineeService");
        superService.changePassword(trainee.getUser(), request.getNewPassword());
    }

    @Transactional
    public void setActive(ActivateDeactivateRequest request) throws VerificationException {
        Trainee trainee = superService.selectByUsername(request.getUsername());
        if (trainee==null) throw new ProhibitedActionException("No one except Trainee could not use TraineeService");
        superService.setActive(trainee.getUser(), request.isActive());
    }

    @Transactional
    public void delete(String username) throws VerificationException {
        Trainee trainee = superService.selectByUsername(username);
        superService.delete(trainee.getId());
    }

    @Transactional(readOnly = true)
    public TraineeProfileResponse selectByUsername(String username) throws VerificationException {
        Trainee trainee = superService.selectByUsername(username);
        return traineeMapper.traineeToProfileResponse(trainee);
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
