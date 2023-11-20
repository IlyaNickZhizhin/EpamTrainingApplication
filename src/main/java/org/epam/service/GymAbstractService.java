package org.epam.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.GymAbstractDaoImpl;
import org.epam.dao.UserDaoImpl;
import org.epam.exceptions.ProhibitedAction;
import org.epam.exceptions.ResourceNotFoundException;
import org.epam.exceptions.VerificationException;
import org.epam.model.User;
import org.epam.model.gymModel.Model;
import org.epam.model.gymModel.UserSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * This class is the abstract superclass for all Service classes.
 * @param <M>
 * @see org.epam.model.gymModel.Model
 * @see org.epam.model.gymModel.UserSetter
 * @see org.epam.dao.GymAbstractDaoImpl
 * @see org.epam.service.TraineeService
 * @see org.epam.service.TrainerService
 * @see org.epam.service.TrainingService
 * @see org.epam.service.GymAbstractService#create(String, String, Object...)
 * @see org.epam.service.GymAbstractService#update(int, Model)
 * @see org.epam.service.GymAbstractService#delete(int)
 * @see org.epam.service.GymAbstractService#select(int)
 * @see org.epam.service.GymAbstractService#selectAll()
 * @see org.epam.service.GymAbstractService#selectByUsername(String)
 * @see org.epam.service.GymAbstractService#changePassword(String, String)
 * @see org.epam.service.GymAbstractService#changeActive(String)
 * @see org.epam.service.GymAbstractService#setActive(String, boolean)
 */
@Service
@Transactional
@Slf4j
public abstract class GymAbstractService<M extends Model> {
    protected GymAbstractDaoImpl<M> gymDao;
    protected UserDaoImpl userDao;
    protected PasswordChecker passwordChecker;
    protected Class<M> modelClass;
    @Autowired
    public void setUserDao(UserDaoImpl userDao) {
        this.userDao = userDao;
    }
    @Autowired
    public void setPasswordChecker(PasswordChecker passwordChecker) {
        this.passwordChecker = passwordChecker;
    }

    public GymAbstractService() {
        this.modelClass = (Class<M>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * This method creates a new model in the database. It logs an informational message before saving the model.
     * There are two options for creating a model:
     * for UserSetter (Trainee and Trainer) and for other Trainings and others.
     * method is protected and must be overridden in child classes by two phases 1) create 2) parametrize
     * @param name (String)
     * @param surname (String)
     * @param parameters (Object...)
     * @return The model that was created and saved.
     */
    protected final M create(String name, String surname, Object... parameters) {
        log.info("Creating " + modelClass.getSimpleName());
        M model;
        User user;
        if (UserSetter.class.isAssignableFrom(modelClass)) {
            log.info("Creating " + modelClass.getSimpleName() + " with user " + name + " " + surname);
            user = userDao.setNewUser(name, surname);
            model = createModel(user, parameters);
            ((UserSetter) model).setUser(user);
        } else {
            UserSetter userSetter = gymDao.getModelByUsername(name);
            log.info("Creating " + modelClass.getSimpleName() + " with user "
                    + name + "who is" + userSetter.getClass().getSimpleName());
            model = createModel(userSetter, parameters);
        }
        log.info("Created " + modelClass.getSimpleName() + "and parametrized");
        try {
            gymDao.save(model);
        } catch (ResourceNotFoundException e) {
            log.error("Error creating " + modelClass.getSimpleName(), e);
            throw e;
        }
        return model;
    }

    /**
     * This method must be overridden in child classes for correct parametrization of creating model
     * @param object (Object)
     * @param parameters (Object...)
     * @return parametrized model
     */
    protected abstract M createModel(Object object, Object... parameters);

    /**
     * This method updates a model in the database using its ID and an updated model object.
     * It logs an informational message before the update operation.
     * @param id The ID of the model to be updated.
     * @param updatedModel The model object containing the updated data.
     * @return The model that was updated.
     */
    protected M update(int id, M updatedModel) {
        log.info("Updating " + modelClass.getSimpleName() + " with id: " + id);
        try {
            gymDao.update(id, updatedModel);
        } catch (ResourceNotFoundException e) {
            log.error("Error updating " + modelClass.getSimpleName() + " with id: " + id, e);
            throw e;
        }
        return updatedModel;
    }

    /**
     * This method deletes a model from the database using its ID.
     * It logs an informational message before the delete operation.
     * @param id The ID of the model to be deleted.
     */
    protected void delete(int id) {
        log.info("Deleting " + modelClass.getSimpleName() + " with id " + id);
        UserSetter model = (UserSetter) gymDao.get(id);
        try {
            gymDao.delete(id);
        } catch (ResourceNotFoundException e) {
            log.error("Error deleting " + modelClass.getSimpleName() + " with id " + id, e);
            throw e;
        }
    }

    /**
     * This method selects a model from the database using its ID.
     * @param id The ID of the model to be selected.
     * @return The model that was selected.
     */
    protected M select(int id) {
        log.info("Selecting " + modelClass.getSimpleName() + " with id " + id);
        try {
            return gymDao.get(id);
        } catch (ResourceNotFoundException e) {
            log.error("Error selecting " + modelClass.getSimpleName() + " with id " + id, e);
            throw e;
        }
    }

    /**
     * This method selects all models from the database.
     * @return The list of models that was selected.
     */
    List<M> selectAll() {
        log.info("Selecting all " + modelClass.getSimpleName() + "s");
        try {
            return gymDao.getAll();
        } catch (ResourceNotFoundException e) {
            log.error("Error selecting all " + modelClass.getSimpleName() + "s", e);
            throw e;
        }
    }

    /**
     * This method selects a model from the database using its username.
     * @param username The username of the model to be selected.
     * @return The model that was selected.
     */
    protected M selectByUsername(String username) {
        log.info("Selecting " + modelClass.getSimpleName() + " with username " + username);
        User user = userDao.getByUsername(username);
        try {
            return gymDao.getByUserId(user.getId());
        } catch (ResourceNotFoundException e) {
            log.error("Error selecting " + modelClass.getSimpleName() + " with username " + username, e);
            throw e;
        }
    }

    /**
     * This method changes password for user with username.
     * @param username (String)
     * @param newPassword (String)
     */
    // TODO нормально ли это что из TraineeService можно менять пароль и активность Trainer и наоборот?
    protected void changePassword(String username, String newPassword) {
        log.info("Changing password for " + username);
        User user = userDao.getByUsername(username);
        if (user.getPassword().equals(newPassword)) {
            throw new ProhibitedAction("It is not possible to change password for user it is already ");
        }
        user.setPassword(newPassword);
        try {
            userDao.update(user.getId(), user);
        } catch (ResourceNotFoundException e) {
            log.error("Error changing password for " + username, e);
            throw e;
        }
    }

    /**
     * This method changes active for user with username.
     * @param username (String)
     */
    protected void changeActive(String username){
        log.info("Changing active for " + username);
        User user = userDao.getByUsername(username);
        user.setActive(!user.isActive());
        try {
            userDao.update(user.getId(), user);
        } catch (ResourceNotFoundException e) {
            log.error("Error changing active for " + username, e);
            throw e;
        }
    }

    // TODO не понимаю какой из двух вариантов выше или ниже - корректный?
    //   НЕ идемпотентная операция — это действие, многократное повторение которого НЕ эквивалентно однократному.

    /**
     * This method sets active for user with username.
     * @param username (String)
     * @param isActive (boolean)
     */
    protected void setActive(String username, boolean isActive) {
        try {
            User user = userDao.getByUsername(username);
            if (user.isActive() != isActive) userDao.update(user.getId(), user);
            else //return;
                throw new ProhibitedAction("It is not possible to set active to "
                        + isActive + " for user it is already " + user.isActive());
        } catch (ResourceNotFoundException e) {
            log.error("Error setting active for " + username, e);
            throw e;
        }
    }

    /**
     * This method verifies the password for a user with the given username.
     * @param username (String)
     * @param password (String)
     * @throws VerificationException when username and password are incorrect
     */
    protected final void verify(String username, String password) throws VerificationException {
        passwordChecker.checkPassword(username, password);
    }

}
