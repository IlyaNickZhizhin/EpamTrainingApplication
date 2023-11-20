package org.epam.service;

import org.epam.dao.TrainerDaoImpl;
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
 * @see org.epam.dao.TrainerDaoImpl
 * @see org.epam.service.TrainerService#create(String, String, TrainingType)
 * @see org.epam.service.TrainerService#update(String, String, int, Trainer)
 * @see org.epam.service.TrainerService#select(String, String, int)
 * @see org.epam.service.TrainerService#selectAll(String, String)
 * @see org.epam.service.TrainerService#selectByUsername(String, String)
 * @see org.epam.service.TrainerService#changePassword(String, String, String)
 * @see org.epam.service.TrainerService#changeActive(String, String)
 * @see org.epam.service.TrainerService#setActive(String, String, boolean)
 */
@Service
public class TrainerService extends GymAbstractService<Trainer> {

    @Autowired
    public void setTrainerDao(TrainerDaoImpl trainerDao) {
        super.gymDao = trainerDao;
    }

    /**
     * Creates new Trainer and send it to database
     * @param firstName (String)
     * @param lastName (String)
     * @param trainingType (TrainingType)
     * @return Trainer
     */
    public Trainer create(String firstName, String lastName, TrainingType trainingType) {
        return super.create(firstName, lastName, trainingType);
    }

    /** Creates new Trainer and send it to database
     * @param user (User)
     * @param parameters (Object...)
     * @return Trainer
     */
    @Override
    protected Trainer createModel(Object user, Object... parameters) {
        Trainer trainer = new Trainer();
        trainer.setUser((User) user);
        trainer.setSpecialization((TrainingType) parameters[0]);
        return trainer;
    }

    /**
     * Updates Trainer in database
     * @param oldUsername (String)
     * @param oldPassword (String)
     * @param id (int)
     * @param upadatedModel (Trainer)
     * @return Trainer
     * @throws VerificationException if username or password is incorrect
     */
    public Trainer update(String oldUsername, String oldPassword, int id, Trainer upadatedModel) throws VerificationException {
        super.verify(oldUsername, oldPassword);
        return super.update(id, upadatedModel);
    }

    /**
     * Deletes Trainer from database
     * @param username (String)
     * @param password (String)
     * @param id (int)
     * @return Trainer
     * @throws VerificationException if username or password is incorrect
     */
    public Trainer select(String username, String password, int id) throws VerificationException {
        super.verify(username, password);
        return super.select(id);
    }

    /**
     * Selects all Trainers from database
     * @param username (String)
     * @param password (String)
     * @return List<Trainer>
     * @throws VerificationException if username or password is incorrect
     */
    protected List<Trainer> selectAll(String username, String password) throws VerificationException {
        super.verify(username, password);
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
        super.verify(username, password);
        return super.selectByUsername(username);
    }

    /**
     * Changes password for user with username
     * @param username (String)
     * @param oldPassword (String)
     * @param newPassword (String)
     * @throws VerificationException if username or password is incorrect
     */
    public void changePassword(String username, String oldPassword, String newPassword)throws VerificationException {
        super.verify(username, oldPassword);
        super.changePassword(username, newPassword);
    }

    /**
     * Changes active status for user with username
     * @param username (String)
     * @param password (String)
     * @throws VerificationException if username or password is incorrect
     */
    public void changeActive(String username, String password) throws VerificationException {
        super.verify(username, password);
        super.changeActive(username);
    }

    /**
     * Changes active status for user with username
     * @param username (String)
     * @param password (String)
     * @param isActive (boolean)
     * @throws VerificationException if username or password is incorrect
     */
    public void setActive(String username, String password, boolean isActive) throws VerificationException {
        super.verify(username, password);
        super.setActive(username, isActive);
    }
}
