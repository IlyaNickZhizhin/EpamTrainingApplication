package org.epam.dao;

import lombok.extern.slf4j.Slf4j;
import org.epam.exceptions.InvalidDataException;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This class is the DAO for Training objects.
 * It contains methods for creating, saving, updating, deleting, and retrieving Training objects.
 * @see org.epam.model.gymModel.Trainee
 * @see GymAbstractDao
 * @see TraineeDaoImpl#update(int, Trainee)
 */
@Repository
@Slf4j
public class TraineeDaoImpl extends GymAbstractDao<Trainee> {

    public TraineeDaoImpl(SessionFactory sessionFactory, UserDao userDao) {
        super(sessionFactory, userDao);
    }

    @Override
    public Trainee update(int id, Trainee updatedTrainee) {
        log.info("Updating trainee with id: " + id);
        try {
            Optional<Trainee> optionalTrainee = Optional.ofNullable(sessionFactory.getCurrentSession().merge(updatedTrainee));
            return optionalTrainee.orElseThrow(() -> new InvalidDataException("update(" + id + ", "
                    + updatedTrainee.getUser().getUsername() + " and other fields)",
                    "Check trainee with id: " + id));
        } catch (Exception e) {
            log.error("Error updating trainee with id: " + id, e);
            throw e;
        }
    }

    public Trainee getModelByUserId(int userId) {
        log.info("Getting Trainee with user №" + userId);
        User user = userDao.get(userId);
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
