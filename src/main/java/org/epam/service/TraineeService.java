package org.epam.service;

import lombok.extern.slf4j.Slf4j;
import org.epam.dao.TraineeDaoImpl;
import org.epam.exceptions.InvaildDeveloperException;
import org.epam.exceptions.InvalidDataException;
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
 * @see org.epam.dao.TraineeDaoImpl
 * @see org.epam.service.TraineeService#create(String, String)
 * @see org.epam.service.TraineeService#create(String, String, String)
 * @see org.epam.service.TraineeService#create(String, String, LocalDate)
 * @see org.epam.service.TraineeService#create(String, String, String, LocalDate)
 * @see org.epam.service.TraineeService#update(String, String, int, Trainee)
 * @see org.epam.service.TraineeService#delete(String, String, int)
 * @see org.epam.service.TraineeService#select(String, String, int)
 * @see org.epam.service.TraineeService#selectByUsername(String, String)
 * @see org.epam.service.TraineeService#changePassword(String, String, String)
 * @see org.epam.service.TraineeService#changeActive(String, String)
 * @see org.epam.service.TraineeService#setActive(String, String, boolean)
 */
@Service
@Slf4j
public class TraineeService extends GymAbstractService<Trainee> {

    @Autowired
    public void setTraineeDao(TraineeDaoImpl traineeDao) {
        super.gymDao = traineeDao;
    }

    /**
     * This method creates a new model in the database. Without parameters.
     * @param firstName (String)
     * @param lastName (String)
     * @return Trainee
     */
    public Trainee create(String firstName, String lastName) {
        log.info("Creating " + modelClass.getSimpleName() + " with user " + firstName + " " + lastName);
        return super.create(firstName, lastName);
    }

    /**
     * This method creates a new model in the database. With one parameter - address.
     * @param firstName (String)
     * @param lastName (String)
     * @param address (String)
     * @return Trainee
     */
    public Trainee create(String firstName, String lastName, String address) {
        log.info("Creating " + modelClass.getSimpleName() + " with user " + firstName + " " + lastName +
                " and address ");
        return super.create(firstName, lastName, address);
    }

    /**
     * This method creates a new model in the database. With one parameter - dateOfBirth.
     * @param firstName (String)
     * @param lastName (String)
     * @param dateOfBirth (LocalDate)
     * @return Trainee
     */
    public Trainee create(String firstName, String lastName, LocalDate dateOfBirth) {
        log.info("Creating " + modelClass.getSimpleName() + " with user " + firstName + " " + lastName +
                " and date of birth " + dateOfBirth);
        return super.create(firstName, lastName, dateOfBirth);
    }

    /**
     * This method creates a new model in the database. With two parameters - address and dateOfBirth.
     * @param firstName (String)
     * @param lastName (String)
     * @param address (String)
     * @param dateOfBirth (LocalDate)
     * @return Trainee
     */
    public Trainee create(String firstName, String lastName, String address, LocalDate dateOfBirth) {
        return super.create(firstName, lastName, address, dateOfBirth);
    }


    /**
     * This method parametrizes a new model before adding database.
     * It logs an informational message before saving in database.
     * @param user (User)
     * @param parameters (Object...)
     * @return Trainee
     */
    @Override
    protected Trainee createModel(Object user, Object... parameters) {
        Trainee trainee = new Trainee();
        switch (parameters.length)
        {
            case 0:
                break;
            case 1:
                try {
                    if (parameters[0] instanceof String) {
                        trainee.setAddress((String) parameters[0]);
                    } else {
                        trainee.setDateOfBirth((LocalDate) parameters[0]);
                    }
                } catch (ClassCastException e) {
                    throw new InvalidDataException("Invalid data for creating " + modelClass.getSimpleName() +
                            "only string address and LocalData");
                }
                break;
            case 2:
                try {
                    trainee.setAddress((String) parameters[0]);
                    trainee.setDateOfBirth((LocalDate) parameters[1]);
                } catch (ClassCastException e) {
                    throw new InvalidDataException("Invalid data for creating " + modelClass.getSimpleName() +
                            "only string address and LocalData, and address first");
                }
                break;
            default:
                throw new InvaildDeveloperException("It is not possible to be here!!!");
        }
        trainee.setUser((User) user);
        return trainee;
    }

    /**
     * This method updates a Trainee in the database using its ID and an updated Trainee object.
     * It logs an informational message before the update operation.
     * If the Trainee to be updated is not found, it logs an error message and throws a ResourceNotFoundException.
     * @param oldUsername (String)
     * @param oldPassword (String)
     * @param id (int)
     * @param updatedModel (Trainee)
     * @return The Trainee that was updated.
     * @throws VerificationException when username and password are incorrect
     */
    public Trainee update(String oldUsername, String oldPassword, int id, Trainee updatedModel) throws VerificationException {
        super.verify(oldUsername, oldPassword);
        return super.update(id, updatedModel);
    }

    /**
     * This method deletes a Trainee from the database using its ID.
     * @param username (String)
     * @param password (String)
     * @param id (int)
     * @throws VerificationException when username and password are incorrect
     */
    public void delete(String username, String password, int id) throws VerificationException {
        super.verify(username, password);
        super.delete(id);
    }

    /**
     * This method selects a Trainee from the database using its ID.
     * @param username (String)
     * @param password (String)
     * @param id (int)
     * @return The Trainee that was selected.
     * @throws VerificationException when username and password are incorrect
     */
    public Trainee select(String username, String password, int id) throws VerificationException {
        super.verify(username, password);
        return super.select(id);
    }

    /**
     * This method selects all Trainees from the database.
     * @param username (String)
     * @param password (String)
     * @return The list of Trainees that was selected.
     * @throws VerificationException when username and password are incorrect
     */
    public Trainee selectByUsername(String username, String password) throws VerificationException {
        super.verify(username, password);
        return super.selectByUsername(username);
    }

    /**
     * This method selects all Trainees from the database.
     * @param username (String)
     * @param oldPassword (String)
     * @param newPassword (String)
     * @throws VerificationException when username and password are incorrect
     */
    public void changePassword(String username, String oldPassword, String newPassword) throws VerificationException {
        super.verify(username, oldPassword);
        super.changePassword(username, newPassword);
    }

    /**
     * This method selects all Trainees from the database.
     * @param username (String)
     * @param password (String)
     * @throws VerificationException when username and password are incorrect
     */
    public void changeActive(String username, String password) throws VerificationException {
        super.verify(username, password);
        super.changeActive(username);
    }

    /**
     * This method selects all Trainees from the database.
     * @param username (String)
     * @param password (String)
     * @param isActive (boolean)
     * @throws VerificationException when username and password are incorrect
     */
    public void setActive(String username, String password, boolean isActive) throws VerificationException {
        super.verify(username, password);
        super.setActive(username, isActive);
    }
}
