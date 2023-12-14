package org.epam.dao;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.epam.exceptions.InvalidDataException;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
public class TrainerDaoImpl extends GymAbstractDao<Trainer> {

    public TrainerDaoImpl(EntityManager entityManager, UserDao userDao) {
        super(entityManager, userDao);
    }

    @Override
    public Optional<Trainer> update(int id, Trainer updatedTrainer) {
        log.info("Updating trainer with id: " + id);
        Optional<Trainer> optTrainer = get(id);
        if (optTrainer.isPresent()) {
            Trainer trainer = optTrainer.get();
            trainer.setUser(updatedTrainer.getUser());
            trainer.setSpecialization(updatedTrainer.getSpecialization());
            trainer.getTrainings().clear();
            trainer.getTrainings().addAll(updatedTrainer.getTrainings());
            trainer.getTrainees().clear();
            trainer.getTrainees().addAll(updatedTrainer.getTrainees());
            try {
                return Optional.ofNullable(entityManager.merge(trainer));
            } catch (Exception e) {
                log.error("Error updating trainer with id: " + id, e);
                throw e;
            }
        } else {
            log.error("Trainer with id: " + id + " not found");
            throw new InvalidDataException(Trainer.class.getSimpleName()+" update", "id " + id + " was incorrect");
        }
    }

    @Override
    public Optional<Trainer>  getModelByUserId(int userId) {
        log.info("Getting Trainer with user №" + userId);
        Optional<User> optionalUser = userDao.get(userId);
        return optionalUser.isPresent() ? getModelByUser(optionalUser.get()) : Optional.empty();
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
