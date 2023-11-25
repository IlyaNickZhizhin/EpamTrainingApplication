package org.epam.service;

import lombok.extern.slf4j.Slf4j;
import org.epam.dao.TraineeDaoImpl;
import org.epam.dto.traineeDto.TraineeDto;
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
    public TraineeDto create(TraineeDto traineeDto) {
        Trainee trainee = gymDao.create(prepare(traineeDto.getFirstname(), traineeDto.getLastname()));
        return gymGeneralMapper.traineeToTraineeDto(trainee);
    }

    @Transactional
    public TraineeDto update(TraineeDto oldDto, TraineeDto newDto) throws VerificationException {
        User user = selectUserByUsername(oldDto.getUsername());
        Trainee trainee = gymDao.getModelByUser(user);
        if (trainee==null) throw new ProhibitedActionException("No one except Trainee could not use TraineeService");
        super.verify(oldDto.getUsername(), oldDto.getPassword(), user);
        TraineeDto forOutput = gymGeneralMapper.traineeToTraineeDto(
                super.update(oldDto.getId(),
                        gymGeneralMapper.traineeDtoToTrainee(newDto)));
        forOutput.setPassword("*********");
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

    private Trainee prepare(String firstName, String lastName) {
        log.info("Creating " + getModelName());
        Trainee trainee = new Trainee();
        User user = userDao.setNewUser(firstName, lastName);
        log.info("Creating " + getModelName() + " with user " + firstName + " " + lastName);
        trainee.setUser(user);
        log.info("Created " + getModelName() + "and parametrized");
        return trainee;
    }

    @Override
    protected String getModelName() {
        return "Trainee";
    }
}
