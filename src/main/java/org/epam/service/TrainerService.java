package org.epam.service;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.epam.dao.TrainerDaoImpl;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.exceptions.ResourceNotFoundException;
import org.epam.exceptions.VerificationException;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class is the Service for Trainer models.
 * @see org.epam.model.gymModel.Trainer
 * @see org.epam.service.GymAbstractService
 * @see TrainerDaoImpl
 * @see org.epam.service.TrainerService#create(String, String, TrainingType)
 * @see org.epam.service.TrainerService#update(String, String, int, Trainer)
 * @see org.epam.service.TrainerService#select(String, String, int)
 * @see org.epam.service.TrainerService#selectAll(String, String)
 * @see org.epam.service.TrainerService#selectByUsername(String, String)
 * @see org.epam.service.TrainerService#changePassword(String, String, String)
 * @see org.epam.service.TrainerService#setActive(String, String, boolean)
 */
@Slf4j
@Service
@Transactional
public class TrainerService extends GymAbstractService<Trainer> {

    @Autowired
    public void setTrainerDao(TrainerDaoImpl trainerDaoImpl) {
        super.gymDao = trainerDaoImpl;
    }

    /**
     * Creates new Trainer and send it to database
     * @param firstName (String)
     * @param lastName (String)
     * @param trainingType (TrainingType)
     * @return Trainer
     */
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

    public Trainer update(String oldUsername, String oldPassword, int id, Trainer upadatedModel) throws VerificationException {
        User user = selectUserByUsername(oldUsername);
        super.verify(oldUsername, oldPassword, user);
        return super.update(id, upadatedModel);
    }

    public Trainer select(String username, String password, int id) throws VerificationException {
        User user = selectUserByUsername(username);
        super.verify(username, password, user);
        return super.select(id);
    }

    protected List<Trainer> selectAll(String username, String password) throws VerificationException {
        User user = selectUserByUsername(username);
        super.verify(username, password, user);
        return super.selectAll();
    }

    /**
     * Selects Trainer from database by username
     * @param username (String)
     * @param password (String)
     * @return Trainer
     * @throws VerificationException if username or password is incorrect
     */
    public Trainer selectByUsername(String username, String password) throws VerificationException {
        User user = selectUserByUsername(username);
        super.verify(username, password, user);
        return gymDao.getModelByUser(user);
    }

    public void changePassword(String username, String oldPassword, String newPassword) throws VerificationException {
        User user;
        try {
            user = super.selectUserByUsername(username);
            verify(username, oldPassword, user);
        } catch (ResourceNotFoundException e) {
            throw new ProhibitedActionException("No one except Trainer could not use TrainerService");
        };
        log.info("Changing password for " + username);
        if (user.getPassword().equals(newPassword)) {
            throw new ProhibitedActionException("It is not possible to change password for user it is already");
        }
        user.setPassword(newPassword);
        userDao.update(user.getId(), user);
    }


    public void setActive(String username, String password, boolean isActive) throws VerificationException {
        User user;
        try {
            user = selectUserByUsername(username);
        } catch (ResourceNotFoundException e) {
            throw new ProhibitedActionException("No one except Trainer could not use TrainerService");
        };
        verify(username, password, user);
        if (user.isActive() != isActive) userDao.update(user.getId(), user);
        log.info("Setting active status for " + username + " to " + isActive);
    }

    @Override
    protected String getModelName() {
        return "Trainer";
    }
}
