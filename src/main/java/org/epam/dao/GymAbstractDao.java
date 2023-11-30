package org.epam.dao;

import lombok.extern.slf4j.Slf4j;
import org.epam.exceptions.InvalidDataException;
import org.epam.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Slf4j
public abstract class GymAbstractDao<M> implements Dao<M>{

    @Autowired
    protected final SessionFactory sessionFactory;
    @Autowired
    protected final UserDao userDao;

    public GymAbstractDao(SessionFactory sessionFactory, UserDao userDao) {
        this.sessionFactory = sessionFactory;
        this.userDao = userDao;
    }

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

    @Override
    public void save(M model) {
        try {
            log.info("Saving " + model);
            sessionFactory.getCurrentSession().persist(model);
        } catch (Exception e) {
            log.error("Error saving " + model, e);
            throw e;
        }

    }

    @Override
    public abstract M update(int id, M model);

    @Override
    public M delete(int id) {
        M model;
        try {
            log.info("Deleting " + getModelName() + " with id " + id);
            model = get(id);
            sessionFactory.getCurrentSession().remove(model);
        } catch (Exception e) {
            log.error("Error deleting " + getModelName() + " with id " + id, e);
            throw e;
        }
        log.info("Deleted " + getModelName() + " with id " + id);
        return model;
    }

    public M get(int id) {
        try {
            log.info("Getting " + getModelName() + " with id " + id);
            Optional<M> modelOptional = Optional.ofNullable(sessionFactory.getCurrentSession().get(getModelClass(), id));
            return modelOptional.orElseThrow(() -> new InvalidDataException("get(" + id + ")", "No " + getModelName() + " with id: " + id));
        } catch (Exception e) {
            log.error("Error getting " + getModelName() + " with id " + id, e);
            throw e;
        }

    }
    public List<M> getAll(){
        try {
            log.info("Getting all " + getModelName() + "s");
            Optional<List<M>> modelOptional = Optional.ofNullable(sessionFactory.getCurrentSession()
                    .createQuery("from " + getModelName(), getModelClass()).list());
            return modelOptional.orElseThrow(()-> new InvalidDataException("getAll()", "No " + getModelName() + "s"));
        } catch (Exception e) {
            log.error("Error getting all " + getModelName() + "s", e);
            throw e;
        }
    }

    public abstract M getModelByUserId(int userId);

    public M getModelByUser(User user) {
        log.info("Getting " + getModelName() + " with user №" + user.getId() + " " + user.getUsername());
        Optional<M> modelOptional = Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("from " + getModelName() + " where user = :user", getModelClass())
                .setParameter("user", user)
                .getSingleResultOrNull());
        return modelOptional.orElseThrow(() -> new InvalidDataException("getModelByUser(" + user.getUsername() +
                " ang other fields)", "No " + getModelName()
                + " with user №" + user.getId() + " " + user.getUsername()));
    }

    protected abstract String getModelName();

    protected abstract Class<M> getModelClass();

}
