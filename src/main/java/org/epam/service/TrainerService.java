package org.epam.service;

import lombok.extern.slf4j.Slf4j;
import org.epam.dao.TrainerDaoImpl;
import org.epam.dto.TrainerDto;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.exceptions.VerificationException;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TrainerService extends GymAbstractService<Trainer> {

    @Autowired
    public void setTrainerDao(TrainerDaoImpl trainerDaoImpl) {
        super.gymDao = trainerDaoImpl;
    }


    @Transactional
    public TrainerDto create(TrainerDto trainerDto) {
        log.info("Creating " + getModelName());
        Trainer trainer = gymMapper.trainerDtoToTrainer(trainerDto);
        User user = userDao.setNewUser(trainerDto.getFirstname(), trainerDto.getLastname());
        log.info("Creating " + getModelName() + " with user " + trainerDto.getFirstname() + " " + trainerDto.getLastname());
        trainer.setUser(user);
        log.info("Created " + getModelName() + "and parametrized");
        return gymMapper.trainerToTrainerDto(gymDao.create(trainer));
    }
    @Transactional
    public TrainerDto update(TrainerDto oldDto, TrainerDto newDto) throws VerificationException {
        User user = selectUserByUsername(oldDto.getUsername());
        super.verify(oldDto.getUsername(), oldDto.getPassword(), user);
        Trainer updatedTrainer = gymMapper.trainerDtoToTrainer(newDto);
        updatedTrainer = super.update(oldDto.getId(), updatedTrainer);
        return gymMapper.trainerToTrainerDto(updatedTrainer);
    }
    @Transactional(readOnly = true)
    public TrainerDto select(TrainerDto trainerDto) throws VerificationException {
        User user = selectUserByUsername(trainerDto.getUsername());
        super.verify(trainerDto.getUsername(), trainerDto.getPassword(), user);
        return gymMapper.trainerToTrainerDto(super.select(trainerDto.getId()));
    }
    @Transactional(readOnly = true)
    public List<TrainerDto> selectAll(TrainerDto trainerDto) throws VerificationException {
        User user = selectUserByUsername(trainerDto.getUsername());
        super.verify(trainerDto.getUsername(), trainerDto.getPassword(), user);
        List<Trainer> trainers = super.selectAll();
        log.info("Selecting all " + getModelName() + "s");
        return gymMapper.trainersToTrainerDtos(trainers);
    }
    @Transactional(readOnly = true)
    public TrainerDto selectByUsername(TrainerDto trainerDto) throws VerificationException {
        User user = selectUserByUsername(trainerDto.getUsername());
        super.verify(trainerDto.getUsername(), trainerDto.getPassword(), user);
        return gymMapper.trainerToTrainerDto(gymDao.getModelByUser(user));
    }
    @Transactional
    public void changePassword(TrainerDto oldDto, TrainerDto newDto) throws VerificationException {
        User user = super.selectUserByUsername(oldDto.getUsername());
        Trainer trainer = super.select(oldDto.getId());
        if (trainer==null) throw new ProhibitedActionException("No one except Trainer could not use TrainerService");
        verify(oldDto.getUsername(), oldDto.getPassword(), user);
        log.info("Changing password for " + oldDto.getUsername());
        if (user.getPassword().equals(newDto.getPassword())) {
            throw new ProhibitedActionException("It is not possible to change password for user it is already");
        }
        user.setPassword(newDto.getPassword());
        userDao.update(user.getId(), user);
    }

    @Transactional
    public void setActive(TrainerDto oldDto, TrainerDto newDto) throws VerificationException {
        User user;
        user = super.selectUserByUsername(oldDto.getUsername());
        Trainer trainer = super.select(oldDto.getId());
        if (trainer==null) throw new ProhibitedActionException("No one except Trainer could not use TrainerService");
        verify(oldDto.getUsername(), oldDto.getPassword(), user);
        if (user.isActive() != newDto.isActive()) userDao.update(user.getId(), user);
        log.info("Setting active status for " + oldDto.isActive() + " to " + newDto.isActive());
    }

    @Override
    protected String getModelName() {
        return "Trainer";
    }
}
