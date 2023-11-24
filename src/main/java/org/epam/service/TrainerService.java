package org.epam.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.epam.dao.TrainerDaoImpl;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.exceptions.VerificationException;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TrainerService extends GymAbstractService<Trainer> {

    @Autowired
    public void setTrainerDao(TrainerDaoImpl trainerDaoImpl) {
        super.gymDao = trainerDaoImpl;
    }

    @Transactional
    public Trainer create(String firstName, String lastName, TrainingType trainingType) {
        log.info("Creating " + getModelName());
        Trainer trainer = new Trainer();
        User user = userDao.setNewUser(firstName, lastName);
        log.info("Creating " + getModelName() + " with user " + firstName + " " + lastName);
        trainer.setUser(user);
        trainer.setSpecialization(trainingType);
        log.info("Created " + getModelName() + "and parametrized");
        return gymDao.create(trainer);
    }
    @Transactional
    public Trainer update(String oldUsername, String oldPassword, int id, Trainer upadatedModel) throws VerificationException {
        User user = selectUserByUsername(oldUsername);
        super.verify(oldUsername, oldPassword, user);
        return super.update(id, upadatedModel);
    }
    @Transactional(readOnly = true)
    public Trainer select(String username, String password, int id) throws VerificationException {
        User user = selectUserByUsername(username);
        super.verify(username, password, user);
        return super.select(id);
    }
    @Transactional(readOnly = true)
    public List<Trainer> selectAll(String username, String password) throws VerificationException {
        User user = selectUserByUsername(username);
        super.verify(username, password, user);
        return super.selectAll();
    }
    @Transactional(readOnly = true)
    public Trainer selectByUsername(String username, String password) throws VerificationException {
        User user = selectUserByUsername(username);
        super.verify(username, password, user);
        return gymDao.getModelByUser(user);
    }
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) throws VerificationException {
        User user;
        try {
            user = super.selectUserByUsername(username);
            verify(username, oldPassword, user);
        } catch (Exception e) {
            throw new ProhibitedActionException("No one except Trainer could not use TrainerService");
        }
        log.info("Changing password for " + username);
        if (user.getPassword().equals(newPassword)) {
            throw new ProhibitedActionException("It is not possible to change password for user it is already");
        }
        user.setPassword(newPassword);
        userDao.update(user.getId(), user);
    }

    @Transactional
    public void setActive(String username, String password, boolean isActive) throws VerificationException {
        User user;
        try {
            user = selectUserByUsername(username);
        } catch (Exception e) {
            throw new ProhibitedActionException("No one except Trainer could not use TrainerService");
        }
        verify(username, password, user);
        if (user.isActive() != isActive) userDao.update(user.getId(), user);
        log.info("Setting active status for " + username + " to " + isActive);
    }

    @Override
    protected String getModelName() {
        return "Trainer";
    }
}
