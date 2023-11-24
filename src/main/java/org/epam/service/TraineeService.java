package org.epam.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.epam.dao.TraineeDaoImpl;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.exceptions.VerificationException;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class TraineeService extends GymAbstractService<Trainee> {

    @Autowired
    public void setTraineeDao(TraineeDaoImpl traineeDaoImpl) {
        super.gymDao = traineeDaoImpl;
    }


    @Transactional
    public Trainee create(String firstName, String lastName) {
        return gymDao.create(prepare(firstName, lastName));
    }


    @Transactional
    public Trainee create(String firstName, String lastName, String address) {
        Trainee trainee = prepare(firstName, lastName);
        trainee.setAddress(address);
        return gymDao.create(trainee);
    }

    @Transactional
    public Trainee create(String firstName, String lastName, LocalDate dateOfBirth) {
        Trainee trainee = prepare(firstName, lastName);
        trainee.setDateOfBirth(dateOfBirth);
        return gymDao.create(trainee);
    }
    @Transactional
    public Trainee create(String firstName, String lastName, String address, LocalDate dateOfBirth) {
        Trainee trainee = prepare(firstName, lastName);
        trainee.setAddress(address);
        trainee.setDateOfBirth(dateOfBirth);
        return gymDao.create(trainee);
    }

    @Transactional
    public Trainee update(String oldUsername, String oldPassword, int id, Trainee updatedModel) throws VerificationException {
        User user = selectUserByUsername(oldUsername);
        super.verify(oldUsername, oldPassword, user);
        return super.update(id, updatedModel);
    }
    @Transactional
    public void delete(String username, String password, int id) throws VerificationException {
        User user = selectUserByUsername(username);
        super.verify(username, password, user);
        super.delete(id);
    }

    @Transactional(readOnly = true)
    public Trainee select(String username, String password, int id) throws VerificationException {
        User user = selectUserByUsername(username);
        super.verify(username, password, user);
        return super.select(id);
    }

    public Trainee selectByUsername(String username, String password) throws VerificationException {
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
            throw new ProhibitedActionException("No one except Trainee could not use TraineeService");
        }
        log.info("Changing password for " + username);
        if (user.getPassword().equals(newPassword)) {
            throw new ProhibitedActionException("It is not possible to change password for user it is already ");
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
            throw new ProhibitedActionException("No one except Trainee could not use TraineeService");
        }
        verify(username, password, user);
        if (user.isActive() != isActive) userDao.update(user.getId(), user);
        log.info("Setting active status for " + username + " to " + isActive);
    }

    @Transactional
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
