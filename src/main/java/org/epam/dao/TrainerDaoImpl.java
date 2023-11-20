package org.epam.dao;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.epam.exceptions.ResourceNotFoundException;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is the DAO for Trainer models.
 * @see org.epam.model.gymModel.Trainer
 * @see org.epam.dao.GymAbstractDaoImpl
 * @see org.epam.dao.TrainerDaoImpl#update(int, Trainer)
 */
@Repository
@Transactional
@Slf4j
public class TrainerDaoImpl extends GymAbstractDaoImpl<Trainer>{

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
            throw new ResourceNotFoundException(Trainer.class.getSimpleName(), id);
        }
    }
}
