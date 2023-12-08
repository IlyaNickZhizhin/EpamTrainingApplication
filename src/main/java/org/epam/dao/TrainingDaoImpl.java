package org.epam.dao;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class TrainingDaoImpl extends GymAbstractDao<Training>{

    public TrainingDaoImpl(EntityManager entityManager, UserDao userDao) {
        super(entityManager, userDao);
    }


    public List<TrainingType> getAllTrainingTypes() {
        try (entityManager) {
            return entityManager.createQuery("from TrainingType", TrainingType.class).getResultList();
        } catch (Exception e) {
            log.error("Something went wrong while getting the list of training types", e);
            throw e;
        }
    }

    @Override
    public Optional<Training> update(int id, Training updatedTraining) {
        Optional<Training> optionalTraining = get(id);
        if (optionalTraining.isPresent()) {
            Training training = optionalTraining.get();
            training.setTrainer(updatedTraining.getTrainer());
            training.setTrainee(updatedTraining.getTrainee());
            training.setTrainingDate(updatedTraining.getTrainingDate());
            try {
               return Optional.ofNullable(entityManager.merge(training));
            } catch (Exception e) {
                log.error("Error updating trainee with id: " + id, e);
                throw e;
            }
        } else {
            log.error("Training with id: " + id + " not found");
            return Optional.empty();
        }
    }

    public Optional<List<Trainer>> getAllTrainersAvalibleForTrainee(Trainee trainee, List<Trainer> trainers) {
        try {
            log.info("Getting all trainers avalible for trainee with id: " + trainee.getId());
            List<Trainer> onTrainee = entityManager
                    .createQuery("from Training where trainee = :trainee", Training.class)
                    .setParameter("trainee", trainee)
                    .getResultStream().map(Training::getTrainer).collect(Collectors.toList());
            trainers.removeAll(onTrainee);
            return Optional.of(trainers);
        } catch (Exception e) {
            log.error("Error getting all trainers avalible for trainee with id: " + trainee.getId(), e);
            throw e;
        }
    }


    public Optional<List<Training>> getAllByUsernameAndTrainingTypes(List<TrainingType> types, Trainer trainer) {
        try (entityManager) {
            List<Training> list = entityManager.createQuery("from Training where trainer = :trainer", Training.class)
                    .setParameter("trainer", trainer)
                    .getResultStream().filter(training -> types.contains(training.getTrainingType()))
                    .collect(Collectors.toList());
            return Optional.of(list);
        } catch (HibernateException e) {
            log.error("Something went wrong while getting the list of trainings", e);
            throw e;
        }
    }

    public Optional<List<Training>> getAllByUsernameAndTrainingTypes(List<TrainingType> types, Trainee trainee) {
        try (entityManager) {
            List<Training> list = entityManager.createQuery("from Training where trainee = :trainee", Training.class)
                    .setParameter("trainee", trainee)
                    .getResultStream().filter(training -> types.contains(training.getTrainingType()))
                    .collect(Collectors.toList());
            return Optional.of(list);
        } catch (HibernateException e) {
            throw new RuntimeException("Something went wrong while getting the list of trainings", e);
        }
    }

    @Override
    public Optional<Training> getModelByUserId(int userId) {
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
