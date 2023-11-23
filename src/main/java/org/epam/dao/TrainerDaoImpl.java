package org.epam.dao;

import lombok.extern.slf4j.Slf4j;
import org.epam.exceptions.InvalidDataException;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * This class is the DAO for Trainer models.
 * @see org.epam.model.gymModel.Trainer
 * @see GymAbstractDao
 * @see TrainerDaoImpl#update(int, Trainer)
 */
@Repository
@Slf4j
public class TrainerDaoImpl extends GymAbstractDao<Trainer> {

    public TrainerDaoImpl(SessionFactory sessionFactory, UserDaoImpl userDao) {
        super(sessionFactory, userDao);
    }

    /**
     * This method updates a Trainer in the database using its ID and an updated Trainer object.
     * It logs an informational message before the update operation.
     * @param id
     * @param updatedTrainer
     */
    @Override
    public void update(int id, Trainer updatedTrainer) {
        log.info("Updating trainer with id: " + id);
        Trainer trainer = get(id);
        if (trainer != null) {
            trainer.setUser(updatedTrainer.getUser());
            trainer.setSpecialization(updatedTrainer.getSpecialization());
            sessionFactory.getCurrentSession().merge(trainer);
        } else {
            log.error("Trainer with id: " + id + " not found");
            throw new InvalidDataException(Trainer.class.getSimpleName());
        }
    }

    @Override
    public Trainer getModelByUserId(int userId) {
        log.info("Getting Trainer with user №" + userId);
        User user = userDao.get(userId);
        return getModelByUser(user);
    }

    @Override
    protected String getModelName() {
        return getModelClass().getSimpleName();
    }

    @Override
    protected Class<Trainer> getModelClass() {
        return Trainer.class;
    }
}
