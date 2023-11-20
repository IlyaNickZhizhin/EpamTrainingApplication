package org.epam.dao;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.epam.exceptions.InvalidDataException;
import org.epam.exceptions.ResourceNotFoundException;
import org.epam.model.User;
import org.epam.model.gymModel.Model;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.UserSetter;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * This class is the abstract superclass for all DAO classes.
 * It contains methods for creating, saving, updating, deleting, and retrieving models.
 * @param <M>
 * @see org.epam.model.gymModel.Model
 * @see org.epam.dao.GymAbstractDaoImpl#create(Model)
 * @see org.epam.dao.GymAbstractDaoImpl#save(Model)
 * @see org.epam.dao.GymAbstractDaoImpl#update(int, Model)
 * @see org.epam.dao.GymAbstractDaoImpl#delete(int)
 * @see org.epam.dao.GymAbstractDaoImpl#get(int)
 * @see org.epam.dao.GymAbstractDaoImpl#getByUserId(int)
 * @see org.epam.dao.GymAbstractDaoImpl#getAll()
 * @see org.epam.dao.UserDaoImpl
 * @see org.epam.dao.TraineeDaoImpl
 * @see org.epam.dao.TrainerDaoImpl
 * @see org.epam.dao.TrainingDaoImpl
 */
@Repository
@Slf4j
public abstract class GymAbstractDaoImpl<M extends Model> implements Dao<M>{

    protected Class<M> modelClass;
    protected SessionFactory sessionFactory;
    private UserDaoImpl userDao;

    public GymAbstractDaoImpl(SessionFactory sessionFactory, UserDaoImpl userDao) {
        this.sessionFactory = sessionFactory;
        this.userDao = userDao;
        this.modelClass = (Class<M>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * This method creates a new model in the database. It logs an informational message before saving the model.
     * If an exception occurs during the save operation, it logs an error message but does not interrupt the execution.
     * After the model is saved, it logs another informational message.
     * @param model The model to be created in the database.
     * @return The model that was created.
     */
    @Override
    public M create(M model) {
        try {
            log.info("Creating " + model);
            save(model);
        } catch (Exception e) {
            log.error("Error creating " + model, e);
        }
        log.info("Created " + model);
        return model;
    }

    /**
     * This method saves a new model in the database. It logs an informational message before saving the model.
     * If an exception occurs during the save operation, it logs an error message but does not interrupt the execution.
     * After the model is saved, it logs another informational message.
     * @param model The model to be created in the database.
     */
    @Override
    @Transactional
    public void save(M model) {
        try {
            log.info("Saving " + model);
            sessionFactory.getCurrentSession().persist(model);
        } catch (Exception e) {
            log.error("Error saving " + model, e);
        }

    }

    /**
     * This method is abstract and should be overridden in subclasses to update a model in the database.
     * @param id The ID of the model to be updated.
     * @param model The model object containing the updated data.
     */
    @Override
    public abstract void update(int id, M model);

    /**
     * This method deletes a model from the database using its ID. It logs an informational message before the delete operation.
     * If an exception occurs during the delete operation, it logs an error message and throws a ResourceNotFoundException.
     * After the model is deleted, it logs another informational message.
     * @param id The ID of the model to be deleted.
     */
    @Override
    @Transactional
    public void delete(int id) {
        try {
            log.info("Deleting " + modelClass.getSimpleName() + " with id " + id);
            M model = get(id);
            sessionFactory.getCurrentSession().remove(model);
        } catch (ResourceNotFoundException e) {
            log.error("Error deleting " + modelClass.getSimpleName() + " with id " + id, e);
            throw new ResourceNotFoundException(modelClass.getSimpleName(), id);
        }
        log.info("Deleted " + modelClass.getSimpleName() + " with id " + id);
    }

    /**
     * This method retrieves a model from the database using its ID. It logs an informational message before the retrieval operation.
     * If an exception occurs during the retrieval operation, it logs an error message and throws a ResourceNotFoundException.
     * @param id The ID of the model to be retrieved.
     * @return The model that was retrieved.
     */
    @Override
    public M get(int id) {
        try {
            log.info("Getting " + modelClass.getSimpleName() + " with id " + id);
            M model = sessionFactory.getCurrentSession().get(modelClass, id);
            if (model == null) {
                throw new ResourceNotFoundException(modelClass.getSimpleName(), id);
            }
            return model;
        } catch (Exception e) {
            log.error("Error getting " + modelClass.getSimpleName() + " with id " + id, e);
            throw e;
        }

    }
    /**
     * This method retrieves all models from the database. It logs an informational message before the retrieval operation.
     * If an exception occurs during the retrieval operation, it logs an error message and throws a ResourceNotFoundException.
     * @return A list of all models.
     */
    public List<M> getAll(){
        try {
            log.info("Getting all " + modelClass.getSimpleName() + "s");
            List<M> models = sessionFactory.getCurrentSession()
                    .createQuery("from " + modelClass.getName(), modelClass).list();
            if (models.isEmpty()) {
                throw new ResourceNotFoundException(modelClass.getSimpleName(), -1);
            }
            return models;
        } catch (Exception e) {
            log.error("Error getting all " + modelClass.getSimpleName() + "s", e);
            throw e;
        }
    }

    /**
     * This method retrieves a model from the database using the ID of a User. It logs an informational message before the retrieval operation.
     * If an exception occurs during the retrieval operation, it logs an error message and throws a ResourceNotFoundException.
     * @param userId The ID of the User.
     * @return The model that was retrieved.
     * @throws ResourceNotFoundException when nothing was founded
     */
    public M getByUserId(int userId) throws ResourceNotFoundException{
        log.info("Getting " + modelClass.getSimpleName() + " with user №" + userId);
        User user = userDao.get(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User", userId);
        }
        return getByUserFast(user);
    }

    /**
     * This method retrieves a model from the database using the username of a User.
     * It logs an informational message before the retrieval operation.
     * @param username
     * @return The model that was retrieved.
     * @throws ResourceNotFoundException when nothing was founded
     */
    public UserSetter getModelByUsername(String username) throws ResourceNotFoundException{
        User user = userDao.getByUsername(username);
        return getUserSetterByUser(user);
    }

    private UserSetter getUserSetterByUser(User user) {
        log.info("Getting UserSetter with user №" + user.getId() + " " + user.getUsername());
        try {
            if (UserSetter.class.isAssignableFrom(modelClass)) {
                log.info("Model is User Setter");
                return (UserSetter) getByUserFast(user);
            } else {
                log.info("Model not UserSetter");
                Trainer trainer = sessionFactory.getCurrentSession()
                        .createQuery("from Trainer where user = :user", Trainer.class)
                        .setParameter("user", user)
                        .getSingleResultOrNull();
                Trainee trainee = sessionFactory.getCurrentSession()
                        .createQuery("from Trainee where user = :user", Trainee.class)
                        .setParameter("user", user)
                        .getSingleResultOrNull();
                return trainee == null ? trainer : trainee;
            }
        } catch (Exception e) {
            log.error("Getting UserSetter with user №" + user.getId() + " " + user.getUsername(), e);
            throw new InvalidDataException("getUserSetterByUser()");
        }
    }
    /**
     * Private method for simplify code of getByUserId(int) getModelByUsername(String)
     * @see org.epam.dao.GymAbstractDaoImpl#getByUserId(int)
     * @see org.epam.dao.GymAbstractDaoImpl#getModelByUsername(String)
     * @param user
     * @return model
     * @trows ResourceNotFoundException when nothing was founded
     */
    private M getByUserFast(User user) {
        log.info("Getting " + modelClass.getSimpleName() + " with user №" + user.getId() + " " + user.getUsername());
        try {
            M model = sessionFactory.getCurrentSession()
                    .createQuery("from " + modelClass.getSimpleName() + " where user = :user", modelClass)
                    .setParameter("user", user)
                    .getSingleResult();
            if (model == null) throw new ResourceNotFoundException(modelClass.getSimpleName(), user.getId());
            return model;
        } catch (Exception e) {
            log.error("Error getting " + modelClass.getSimpleName()
                    + " with user №" + user.getId() + " " + user.getUsername(), e);
            throw e;
        }
    }

}
