package org.epam.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.epam.dao.TraineeDaoImpl;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.exceptions.ResourceNotFoundException;
import org.epam.exceptions.VerificationException;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * This class is the Service for Trainee models.
 * @see org.epam.model.gymModel.Trainee
 * @see org.epam.service.GymAbstractService
 * @see TraineeDaoImpl
 * @see org.epam.service.TraineeService#create(String, String)
 * @see org.epam.service.TraineeService#create(String, String, String)
 * @see org.epam.service.TraineeService#create(String, String, LocalDate)
 * @see org.epam.service.TraineeService#create(String, String, String, LocalDate)
 * @see org.epam.service.TraineeService#update(String, String, int, Trainee)
 * @see org.epam.service.TraineeService#delete(String, String, int)
 * @see org.epam.service.TraineeService#select(String, String, int)
 * @see org.epam.service.TraineeService#selectByUsername(String, String)
 * @see org.epam.service.TraineeService#changePassword(String, String, String)
 * @see org.epam.service.TraineeService#setActive(String, String, boolean)
 */
@Service
@Slf4j
@Transactional
public class TraineeService extends GymAbstractService<Trainee> {

    @Autowired
    public void setTraineeDao(TraineeDaoImpl traineeDaoImpl) {
        super.gymDao = traineeDaoImpl;
    }


    public Trainee create(String firstName, String lastName) {
        return gymDao.create(prepare(firstName, lastName));
    }


    public Trainee create(String firstName, String lastName, String address) {
        Trainee trainee = prepare(firstName, lastName);
        trainee.setAddress(address);
        return gymDao.create(trainee);
    }


    public Trainee create(String firstName, String lastName, LocalDate dateOfBirth) {
        Trainee trainee = prepare(firstName, lastName);
        trainee.setDateOfBirth(dateOfBirth);
        return gymDao.create(trainee);
    }

    public Trainee create(String firstName, String lastName, String address, LocalDate dateOfBirth) {
        Trainee trainee = prepare(firstName, lastName);
        trainee.setAddress(address);
        trainee.setDateOfBirth(dateOfBirth);
        return gymDao.create(trainee);
    }


    public Trainee update(String oldUsername, String oldPassword, int id, Trainee updatedModel) throws VerificationException {
        User user = selectUserByUsername(oldUsername);
        super.verify(oldUsername, oldPassword, user);
        return super.update(id, updatedModel);
    }

    public void delete(String username, String password, int id) throws VerificationException {
        User user = selectUserByUsername(username);
        super.verify(username, password, user);
        super.delete(id);
    }

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

    public void changePassword(String username, String oldPassword, String newPassword) throws VerificationException {
        User user;
        try {
            user = super.selectUserByUsername(username);
            verify(username, oldPassword, user);
        } catch (ResourceNotFoundException e) {
            throw new ProhibitedActionException("No one except Trainee could not use TraineeService");
        };
        log.info("Changing password for " + username);
        if (user.getPassword().equals(newPassword)) {
            throw new ProhibitedActionException("It is not possible to change password for user it is already ");
        }
        user.setPassword(newPassword);
        userDao.update(user.getId(), user);
    }

    public void setActive(String username, String password, boolean isActive) throws VerificationException {
        User user;
        try {
            user = selectUserByUsername(username);
        } catch (ResourceNotFoundException e) {
            throw new ProhibitedActionException("No one except Trainee could not use TraineeService");
        };
        verify(username, password, user);
        if (user.isActive() != isActive) userDao.update(user.getId(), user);
        log.info("Setting active status for " + username + " to " + isActive);
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
