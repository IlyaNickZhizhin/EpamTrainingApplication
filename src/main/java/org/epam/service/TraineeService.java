package org.epam.service;

import lombok.extern.slf4j.Slf4j;
import org.epam.dao.TraineeDaoImpl;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.LoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeDto;
import org.epam.dto.traineeDto.TraineeProfileResponse;
import org.epam.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.dto.traineeDto.UpdateTraineeProfileRequest;
import org.epam.dto.trainerDto.ShotTrainerDto;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.exceptions.VerificationException;
import org.epam.mapper.TraineeMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TraineeService extends GymAbstractService<Trainee> {

    @Autowired
    private TraineeMapper traineeMapper;

    @Autowired
    public void setTraineeDao(TraineeDaoImpl traineeDaoImpl) {
        super.gymDao = traineeDaoImpl;
    }

    @Transactional
    public RegistrationResponse create(TraineeRegistrationRequest request) {
        Trainee trainee = gymDao.create(prepare(request));
        log.info("Created " + getModelName() + " with id " + trainee.getId());
        TraineeDto traineeDto = gymGeneralMapper.traineeToTraineeDto(trainee);
        return traineeMapper.traineeToRegistrationResponse(traineeDto);
    }

    @Transactional
    public TraineeProfileResponse update(LoginRequest login, UpdateTraineeProfileRequest request) throws VerificationException {
        Trainee trainee = loginAndTakeModel(login);
        TraineeDto newDto = traineeMapper.updateTraineeProfileResponseToTraineeDto(request);
        trainee = super.update(trainee.getId(),
                        gymGeneralMapper.traineeDtoToTrainee(newDto));
        TraineeProfileResponse response = traineeMapper
                .traineeDtoToProfileResponse(gymGeneralMapper
                        .traineeToTraineeDto(trainee));
        List<Trainer> trainers = trainee.getTrainings().stream().map(Training::getTrainer).collect(Collectors.toList());
        response.setTrainers(gymGeneralMapper.trainersDtoToShot(
                gymGeneralMapper.trainersToTrainerDtos(trainers)));
        return response;
    }

    @Transactional
    public void changePassword(ChangeLoginRequest request) throws VerificationException {
        Trainee trainee = loginAndTakeModel(gymGeneralMapper.changeLoginToLogin(request));
        User user = trainee.getUser();
        if (user.getPassword().equals(request.getNewPassword())) {
            throw new ProhibitedActionException("It is not possible to change password for user it is already ");
        }
        user.setPassword(request.getNewPassword());
        userDao.update(user.getId(), user);
    }

    @Transactional
    public void setActive(LoginRequest login, ActivateDeactivateRequest request) throws VerificationException {
        Trainee trainee = loginAndTakeModel(login);
        if (trainee==null) throw new ProhibitedActionException("No one except Trainee could not use TraineeService");
        super.setActive(trainee.getUser(), request.isActive());
    }

    @Transactional
    public void delete(LoginRequest login, String username) throws VerificationException {
        Trainee trainee = loginAndTakeModel(login);
        super.delete(trainee.getId());
    }

    public TraineeProfileResponse selectByUsername(LoginRequest request, String username) throws VerificationException {
        Trainee trainee = loginAndTakeModel(request);
        TraineeDto traineeDto = gymGeneralMapper.traineeToTraineeDto(trainee);
        List<Trainer> trainers = trainee.getTrainings().stream().map(Training::getTrainer).collect(Collectors.toList());
        List<ShotTrainerDto> shotTrainerDtos = gymGeneralMapper
                .trainersDtoToShot(gymGeneralMapper
                        .trainersToTrainerDtos(trainers));
        TraineeProfileResponse response = traineeMapper.traineeDtoToProfileResponse(traineeDto);
        response.setTrainers(shotTrainerDtos);
        return response;
    }

    private Trainee prepare(TraineeRegistrationRequest request) {
        log.info("Creating " + getModelName());
        Trainee trainee = new Trainee();
        User user = userDao.setNewUser(request.getFirstName(), request.getLastName());
        log.info("Creating " + getModelName() + " with user " + request.getFirstName() + " " + request.getLastName());
        trainee.setUser(user);
        trainee.setAddress(request.getAddress());
        trainee.setDateOfBirth(request.getDateOfBirth());
        log.info("Created " + getModelName() + "and parametrized");
        return trainee;
    }

    Trainee loginAndTakeModel(LoginRequest login) throws VerificationException, ProhibitedActionException {
        log.info("Login " + getModelName() + " with username " + login.getUsername());
        User user = selectUserByUsername(login.getUsername());
        log.info("Getting " + getModelName() + "with username" + user.getUsername());
        Trainee trainee = gymDao.getModelByUser(user);
        if (trainee==null) throw new ProhibitedActionException("No one except Trainee could not use TraineeService");
        log.info("Verifying " + getModelName() + " with username " + login.getUsername());
        super.verify(login.getUsername(), login.getPassword(), user);
        return trainee;
    }

    @Override
    protected String getModelName() {
        return "Trainee";
    }
}
