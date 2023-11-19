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
public class TraineeDaoImpl extends GymAbstractDaoImpl<Trainee> {

    /**
     * This method updates a Trainee in the database using its ID and an updated Trainee object. It logs an informational message before the update operation.
     * If the Trainee to be updated is not found, it logs an error message and throws a ResourceNotFoundException.
     * @param id The ID of the Trainee to be updated.
     * @param updatedTrainee The Trainee object containing the updated data.
     */
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
            }
        } else {
            log.error("Trainee with id: " + id + " not found");
            throw new ResourceNotFoundException(Trainee.class.getSimpleName(), id);
        }
    }
}
