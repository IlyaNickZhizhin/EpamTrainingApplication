package org.epam.dao;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.epam.exceptions.ResourceNotFoundException;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.epam.model.gymModel.UserSetter;
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
 * @see org.epam.dao.GymAbstractDaoImpl
 * @see org.epam.dao.TrainingDaoImpl#update(int, Training)
 * @see org.epam.dao.TrainingDaoImpl#updateTrainersList(int, Trainee)
 * @see org.epam.dao.TrainingDaoImpl#getAllTrainersAvalibleForTrainee(Trainee, List)
 * @see org.epam.dao.TrainingDaoImpl#getAllByTraineeAndTrainingTypes(Trainee, List)
 * @see org.epam.dao.TrainingDaoImpl#getAllByTrainerAndTrainingTypes(Trainer, List)
 */
@Repository
@Transactional
@Slf4j
public class TrainingDaoImpl extends GymAbstractDaoImpl<Training>{

    public TrainingDaoImpl(SessionFactory sessionFactory, UserDaoImpl userDao) {
        super(sessionFactory, userDao);
    }

    /**
     * This method updates a Training in the database using its ID and an updated Training object.
     * @param id
     * @param updatedTraining
     */
    @Override
    public void update(int id, Training updatedTraining) {
        Training training = get(id);
        if (training != null) {
            training.setTrainer(updatedTraining.getTrainer());
            training.setTrainee(updatedTraining.getTrainee());
            training.setTrainingDate(updatedTraining.getTrainingDate());
            sessionFactory.getCurrentSession().merge(training);
        }
    }

    // TODO я не понял зачем этот метод, и вероятно сделал его не корректно.

    /**
     * This method updates the list of Trainers for a Trainee. It logs an informational message before the update operation.
     * If an exception occurs during the update operation, it logs an error message and throws a ResourceNotFoundException.
     * @param id The ID of the Trainee.
     * @param traineeForUpdateList The Trainee object for which the list of Trainers is to be updated.
     * @return The updated list of Trainers.
     */
    public List<Trainer> updateTrainersList(Trainee traineeForUpdateList) {
        log.info("Updating trainers list for trainee №" + traineeForUpdateList.getId());
        try {
            return sessionFactory.getCurrentSession().createQuery("from Training where trainee = :trainee", Training.class)
                    .setParameter("trainee", traineeForUpdateList)
                    .getResultStream().map(Training::getTrainer).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error updating trainers list for trainee №" + traineeForUpdateList.getId(), e);
            throw new ResourceNotFoundException("List<Trainer> fo trainee №", traineeForUpdateList.getId());
        }
    }

    /**
     * This method gets all available active Trainers, have not set for a Trainee yet.
     * @param trainee
     * @param trainers - all existing in gym trainers
     */
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
            throw new ResourceNotFoundException(Trainee.class.getSimpleName(), trainee.getId());
        }
    }

    public List<Training> getAllByUsernameAndTrainingTypes(String username, List<TrainingType> types) {
        try (Session session = sessionFactory.openSession()) {
            UserSetter userSetter = getModelByUsername(username);
            return session.createQuery("from Training where" +
                            userSetter.getClass().getSimpleName().toLowerCase() +
                            "= :userSetter", Training.class)
                    .setParameter(userSetter.getClass().getSimpleName().toLowerCase(), userSetter)
                    .getResultStream().filter(training -> types.contains(training.getTrainingType()))
                    .collect(Collectors.toList());
        } catch (HibernateException e) {
            throw new RuntimeException("Something went wrong while getting the list of trainings", e);
        }
    }
}
