package org.epam.dao;

import lombok.extern.slf4j.Slf4j;
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
    public Optional<M> create(M model) {
        try {
            log.info("Creating " + model);
            save(model);
        } catch (Exception e) {
            log.error("Error creating " + model, e);
        }
        log.info("Created " + model);
        return Optional.of(model);
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
    public abstract Optional<M> update(int id, M model);

    @Override
    public Optional<M> delete(int id) {
        Optional<M> optionalModel;
        try {
            log.info("Deleting " + getModelName() + " with id " + id);
            optionalModel = get(id);
            if (optionalModel.isPresent()) {
                sessionFactory.getCurrentSession().remove(optionalModel.get());
            } else return Optional.empty();
        } catch (Exception e) {
            log.error("Error deleting " + getModelName() + " with id " + id, e);
            throw e;
        }
        log.info("Deleted " + getModelName() + " with id " + id);
        return optionalModel;
    }

    public Optional<M> get(int id) {
        try {
            log.info("Getting " + getModelName() + " with id " + id);
            return Optional.ofNullable(sessionFactory.getCurrentSession().get(getModelClass(), id));
        } catch (Exception e) {
            log.error("Error getting " + getModelName() + " with id " + id, e);
            throw e;
        }

    }
    public Optional<List<M>> getAll(){
        try {
            log.info("Getting all " + getModelName() + "s");
            return Optional.ofNullable(sessionFactory.getCurrentSession()
                    .createQuery("from " + getModelName(), getModelClass()).list());
        } catch (Exception e) {
            log.error("Error getting all " + getModelName() + "s", e);
            throw e;
        }
    }

    public abstract Optional<M> getModelByUserId(int userId);

    public Optional<M> getModelByUser(User user) {
        log.info("Getting " + getModelName() + " with user: " + user.getUsername() +" №" + user.getId());
        return Optional.ofNullable(sessionFactory.getCurrentSession()
                .createQuery("from " + getModelName() + " where user = :user", getModelClass())
                .setParameter("user", user)
                .getSingleResultOrNull());
    }

    protected abstract String getModelName();

    protected abstract Class<M> getModelClass();

}
