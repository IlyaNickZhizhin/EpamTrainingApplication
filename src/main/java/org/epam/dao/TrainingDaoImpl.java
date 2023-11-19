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
