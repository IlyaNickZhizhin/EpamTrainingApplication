package org.epam.service;

import lombok.extern.slf4j.Slf4j;
import org.epam.dao.TraineeDaoImpl;
import org.epam.dto.LoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeDto;
import org.epam.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.dto.traineeDto.UpdateTraineeProfileRequest;
import org.epam.dto.traineeDto.UpdateTraineeProfileResponse;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.exceptions.VerificationException;
import org.epam.mapper.TraineeMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public UpdateTraineeProfileResponse update(LoginRequest login, UpdateTraineeProfileRequest request) throws VerificationException {
        User user = selectUserByUsername(login.getUsername());
        Trainee trainee = gymDao.getModelByUser(user);
        if (trainee==null) throw new ProhibitedActionException("No one except Trainee could not use TraineeService");
        super.verify(login.getUsername(), login.getPassword(), user);
        TraineeDto newDto = gymGeneralMapper.traineeToTraineeDto(trainee);
        trainee = super.update(trainee.getId(),
                        gymGeneralMapper.traineeDtoToTrainee(newDto));
        UpdateTraineeProfileResponse forOutput = traineeMapper
                .traineeDtoToUpdateResponse(gymGeneralMapper
                        .traineeToTraineeDto(trainee));
        return forOutput;
    }

    @Transactional
    public void changePassword(TraineeDto oldDto, TraineeDto newDto) throws VerificationException {
        User user = selectUserByUsername(oldDto.getUsername());
        Trainee trainee = gymDao.getModelByUser(user);
        if (trainee==null) throw new ProhibitedActionException("No one except Trainee could not use TraineeService");
        verify(oldDto.getUsername(), oldDto.getPassword(), user);
        if (user.getPassword().equals(newDto.getPassword())) {
            throw new ProhibitedActionException("It is not possible to change password for user it is already ");
        }
        user.setPassword(newDto.getPassword());
        userDao.update(user.getId(), user);
    }

    @Transactional
    public void setActive(TraineeDto oldDto, TraineeDto newDto) throws VerificationException {
        User user = selectUserByUsername(oldDto.getUsername());
        Trainee trainee = gymDao.getModelByUser(user);
        if (trainee==null) throw new ProhibitedActionException("No one except Trainee could not use TraineeService");
        verify(oldDto.getUsername(), oldDto.getPassword(), user);
        if (user.isActive() != newDto.isActive()) userDao.update(user.getId(), user);
        userDao.update(user.getId(), user);
        log.info("Setting active status for " + oldDto.getUsername() + " to " + newDto.isActive());
    }

    @Transactional
    public void delete(TraineeDto traineeDto) throws VerificationException {
        User user = selectUserByUsername(traineeDto.getUsername());
        super.verify(traineeDto.getUsername(), traineeDto.getPassword(), user);
        super.delete(traineeDto.getId());
    }

    @Transactional(readOnly = true)
    public TraineeDto select(TraineeDto traineeDto) throws VerificationException {
        User user = selectUserByUsername(traineeDto.getUsername());
        super.verify(traineeDto.getUsername(), traineeDto.getPassword(), user);
        TraineeDto forOutput = gymGeneralMapper.traineeToTraineeDto(super.select(traineeDto.getId()));
        forOutput.setPassword("*********");
        return forOutput;
    }

    public TraineeDto selectByUsername(TraineeDto traineeDto) throws VerificationException {
        User user = selectUserByUsername(traineeDto.getUsername());
        super.verify(traineeDto.getUsername(), traineeDto.getPassword(), user);
        TraineeDto forOutput = gymGeneralMapper.traineeToTraineeDto(super.select(traineeDto.getId()));
        forOutput.setPassword("*********");
        return forOutput;
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

    @Override
    protected String getModelName() {
        return "Trainee";
    }
}
