package org.epam.dao;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.epam.exceptions.ResourceNotFoundException;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional
@Slf4j
public class TrainingDaoImpl extends GymAbstractDaoImpl<Training>{

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
    public List<Trainer> updateTrainersList(int id, Trainee traineeForUpdateList) {
        log.info("Updating trainers list for trainee with id: " + id);
        try {
            return sessionFactory.getCurrentSession().createQuery("from Training where trainee = :trainee", Training.class)
                    .setParameter("trainee", traineeForUpdateList)
                    .getResultStream().map(Training::getTrainer).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error updating trainers list for trainee with id: " + id, e);
            throw new ResourceNotFoundException("List<Trainer>", id);
        }
    }

    /**
     * This method gets all available active Trainers, have not set for a Trainee yet.
     * @param trainee
     * @param trainers
     */
    public List<Trainer> getAllTrainersAvalibleForTrainee(Trainee trainee, List<Trainer> trainers) {
        try {
            log.info("Getting all trainers avalible for trainee with id: " + trainee.getId());
            //return from list of All trainers
            return trainers.stream()
                    //only active trainers
                    .filter(er -> er.getUser().isActive()
                            //trainers who are not in trainee's training list
                            && !(sessionFactory.getCurrentSession()
                            .createQuery("from Training where trainee = :trainee", Training.class)
                            .setParameter("trainee", trainee)
                            .getResultStream().map(Training::getTrainer).collect(Collectors.toList()))
                            .contains(er)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting all trainers avalible for trainee with id: " + trainee.getId(), e);
            throw new ResourceNotFoundException(Trainee.class.getSimpleName(), trainee.getId());
        }
    }


    /**
     * This method gets all the Trainings for a Trainer, by current trainee and list of trainingTypes.
     * @param trainee
     * @param trainingTypes
     * @return List<Training>
     */
    public List<Training> getAllByTraineeAndTrainingTypes(Trainee trainee, List<TrainingType> trainingTypes) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Training where trainee = :trainee", Training.class)
                    .setParameter("trainee", trainee)
                    .getResultStream().filter(training -> trainingTypes.contains(training.getTrainingType()))
                    .collect(Collectors.toList());
        } catch (HibernateException e) {
            throw new RuntimeException("Something went wrong while getting the list of trainings", e);
        }
    }

    /**
     * This method gets all the Trainings for a Trainee, by current TrainingType.
     * @param trainer
     * @param trainingTypes
     * @return List<Training>
     */
    public List<Training> getAllByTrainerAndTrainingTypes(Trainer trainer, List<TrainingType> trainingTypes) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Training where trainer = :trainer", Training.class)
                    .setParameter("trainer", trainer)
                    .getResultStream().filter(training -> trainingTypes.contains(training.getTrainingType()))
                    .collect(Collectors.toList());
        } catch (HibernateException e) {
            throw new RuntimeException("Something went wrong while getting the list of trainings", e);
        }
    }
}
