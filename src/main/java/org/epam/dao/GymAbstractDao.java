package org.epam.dao;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.epam.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Slf4j
public abstract class GymAbstractDao<M> implements Dao<M>{

    @Autowired
    protected final SessionFactory sessionFactory;
    @Autowired
    protected final UserDaoImpl userDao;

    public GymAbstractDao(SessionFactory sessionFactory, UserDaoImpl userDao) {
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
    public abstract void update(int id, M model);

    @Override
    public void delete(int id) {
        try {
            log.info("Deleting " + getModelName() + " with id " + id);
            M model = get(id);
            sessionFactory.getCurrentSession().remove(model);
        } catch (Exception e) {
            log.error("Error deleting " + getModelName() + " with id " + id, e);
            throw e;
        }
        log.info("Deleted " + getModelName() + " with id " + id);
    }

    public M get(int id) {
        try {
            log.info("Getting " + getModelName() + " with id " + id);
            return sessionFactory.getCurrentSession().get(getModelClass(), id);
        } catch (Exception e) {
            log.error("Error getting " + getModelName() + " with id " + id, e);
            throw e;
        }

    }
    public List<M> getAll(){
        try {
            log.info("Getting all " + getModelName() + "s");
            return sessionFactory.getCurrentSession()
                    .createQuery("from " + getModelName(), getModelClass()).list();
        } catch (Exception e) {
            log.error("Error getting all " + getModelName() + "s", e);
            throw e;
        }
    }

    public abstract M getModelByUserId(int userId);

    public M getModelByUser(User user) {
        log.info("Getting " + getModelName() + " with user №" + user.getId() + " " + user.getUsername());
        return sessionFactory.getCurrentSession()
                .createQuery("from " + getModelName() + " where user = :user", getModelClass())
                .setParameter("user", user)
                .getSingleResultOrNull();
    }

    protected abstract String getModelName();
    
    protected abstract Class<M> getModelClass();

}
