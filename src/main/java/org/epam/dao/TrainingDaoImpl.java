package org.epam.dao;

import lombok.extern.slf4j.Slf4j;
import org.epam.exceptions.InvalidDataException;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is the DAO for Training objects.
 * It contains methods for creating, saving, updating, deleting, and retrieving Training objects.
 * @see org.epam.model.gymModel.Training
 * @see GymAbstractDao
 * @see TrainingDaoImpl#update(int, Training)
 * @see TrainingDaoImpl#getAllTrainersAvalibleForTrainee(Trainee, List)
 */
@Repository
@Slf4j
public class TrainingDaoImpl extends GymAbstractDao<Training>{

    public TrainingDaoImpl(SessionFactory sessionFactory, UserDaoImpl userDao) {
        super(sessionFactory, userDao);
    }


    public List<TrainingType> getAllTrainingTypes() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from TrainingType", TrainingType.class).getResultList();
        } catch (Exception e) {
            log.error("Something went wrong while getting the list of training types", e);
            throw e;
        }
    }

    @Override
    public Training update(int id, Training updatedTraining) {
        Training training = get(id);
        if (training != null) {
            training.setTrainer(updatedTraining.getTrainer());
            training.setTrainee(updatedTraining.getTrainee());
            training.setTrainingDate(updatedTraining.getTrainingDate());
            try {
               return sessionFactory.getCurrentSession().merge(training);
            } catch (Exception e) {
                log.error("Error updating trainee with id: " + id, e);
                throw e;
            }
        } else {
            log.error("Training with id: " + id + " not found");
            throw new InvalidDataException(Training.class.getSimpleName());
        }
    }

    public List<Trainer> getAllTrainersAvalibleForTrainee(Trainee trainee, List<Trainer> trainers) {
        try {
            log.info("Getting all trainers avalible for trainee with id: " + trainee.getId());
            List<Trainer> onTrainee = sessionFactory.getCurrentSession()
                    .createQuery("from Training where trainee = :trainee", Training.class)
                    .setParameter("trainee", trainee)
                    .getResultStream().map(Training::getTrainer).collect(Collectors.toList());
            trainers.removeAll(onTrainee);
            return trainers;
        } catch (Exception e) {
            log.error("Error getting all trainers avalible for trainee with id: " + trainee.getId(), e);
            throw e;
        }
    }


    public List<Training> getAllByUsernameAndTrainingTypes(List<TrainingType> types, Trainer trainer) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Training where trainer = :trainer", Training.class)
                        .setParameter("trainer", trainer)
                        .getResultStream().filter(training -> types.contains(training.getTrainingType()))
                        .collect(Collectors.toList());
        } catch (HibernateException e) {
            log.error("Something went wrong while getting the list of trainings", e);
            throw e;
        }
    }

    public List<Training> getAllByUsernameAndTrainingTypes(List<TrainingType> types, Trainee trainee) {
        try (Session session = sessionFactory.openSession()) {
        return session.createQuery("from Training where trainee = :trainee", Training.class)
                .setParameter("trainee", trainee)
                .getResultStream().filter(training -> types.contains(training.getTrainingType()))
                .collect(Collectors.toList());
        } catch (HibernateException e) {
            throw new RuntimeException("Something went wrong while getting the list of trainings", e);
        }
    }

    @Override
    public Training getModelByUserId(int userId) {
        throw new ProhibitedActionException("You can't get Training by User ID");
    }


    @Override
    protected String getModelName() {
        return getModelClass().getSimpleName();
    }

    @Override
    protected Class<Training> getModelClass() {
        return Training.class;
    }
}
