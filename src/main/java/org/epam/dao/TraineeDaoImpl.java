package org.epam.dao;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
public class TraineeDaoImpl extends GymAbstractDao<Trainee> {

    public TraineeDaoImpl(EntityManager entityManager, UserDao userDao) {
        super(entityManager, userDao);
    }

    @Override
    public Optional<Trainee> update(int id, Trainee updatedTrainee) {
        log.info("Updating trainee with id: " + id);
        try {
            return Optional.ofNullable(entityManager.merge(updatedTrainee));
        } catch (Exception e) {
            log.error("Error updating trainee with id: " + id, e);
            throw e;
        }
    }

    public Optional<Trainee> getModelByUserId(int userId) {
        log.info("Getting Trainee with user №" + userId);
        User user = userDao.get(userId).orElse(null);
        return getModelByUser(user);
    }

    @Override
    protected String getModelName() {
        return getModelClass().getSimpleName();
    }

    @Override
    protected Class<Trainee> getModelClass() {
        return Trainee.class;
    }
}
