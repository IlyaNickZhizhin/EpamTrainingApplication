package org.epam.dao;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.epam.exceptions.ResourceNotFoundException;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional
@Slf4j
public class TrainerDaoImpl extends GymAbstractDaoImpl<Trainer>{

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

    /**
     * This method gets all available active Trainers, have not set for a Trainee yet.
     * @param trainee
     * @param trainers
     * @return List<Trainer>
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
}
