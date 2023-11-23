package org.epam.dao;

import lombok.extern.slf4j.Slf4j;
import org.epam.exceptions.InvalidDataException;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

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

    public TraineeDaoImpl(SessionFactory sessionFactory, UserDaoImpl userDao) {
        super(sessionFactory, userDao);
    }

    @Override
    public void update(int id, Trainee updatedTrainee) {
        log.info("Updating trainee with id: " + id);
        Trainee trainee = get(id);
        if (trainee != null) {
            trainee.setUser(updatedTrainee.getUser());
            trainee.setAddress(updatedTrainee.getAddress());
            trainee.setDateOfBirth(updatedTrainee.getDateOfBirth());
            try {
                sessionFactory.getCurrentSession().merge(trainee);
            } catch (Exception e) {
                log.error("Error updating trainee with id: " + id, e);
                throw e;
            }
        } else {
            log.error("Trainee with id: " + id + " not found");
            throw new InvalidDataException(Trainee.class.getSimpleName());
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
