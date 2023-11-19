package org.epam.dao;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.epam.exceptions.ResourceNotFoundException;
import org.epam.model.User;
import org.epam.model.gymModel.Model;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;

@Repository
@Slf4j
public abstract class GymAbstractDaoImpl<M extends Model> implements Dao<M>{

    protected Class<M> modelClass;
    @Autowired
    protected SessionFactory sessionFactory;

    public GymAbstractDaoImpl() {
        this.modelClass = (Class<M>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }



    @Autowired
    private UserDaoImpl userDao;

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
        } catch (Exception e) {
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
            return sessionFactory.getCurrentSession().get(modelClass, id);
        } catch (Exception e) {
            log.error("Error getting " + modelClass.getSimpleName() + " with id " + id, e);
            throw new ResourceNotFoundException(modelClass.getSimpleName(), id);
        }

    }

    /**
     * This method retrieves a model from the database using the ID of a User. It logs an informational message before the retrieval operation.
     * If an exception occurs during the retrieval operation, it logs an error message and throws a ResourceNotFoundException.
     * @param userId The ID of the User.
     * @return The model that was retrieved.
     */
    public M getByUserId(int userId) {
        User user = userDao.get(userId);
        try {
            log.info("Getting " + modelClass.getSimpleName() + " with user id " + userId);
            return sessionFactory.getCurrentSession()
                    .createQuery("from " + modelClass.getSimpleName() + " where user = :user", modelClass)
                    .setParameter("user", user)
                    .getSingleResult();
        } catch (Exception e) {
            log.error("Error getting " + modelClass.getSimpleName() + " with user id " + userId, e);
            throw new ResourceNotFoundException(modelClass.getSimpleName(), userId);
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
            return sessionFactory.getCurrentSession()
                    .createQuery("from " + modelClass.getName(), modelClass).list();
        } catch (Exception e) {
            log.error("Error getting all " + modelClass.getSimpleName() + "s", e);
            throw new ResourceNotFoundException(modelClass.getSimpleName(), -1);
        }
    }
}
