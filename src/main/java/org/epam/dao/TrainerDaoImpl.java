package org.epam.dao;

import lombok.extern.slf4j.Slf4j;
import org.epam.exceptions.InvalidDataException;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
public class TrainerDaoImpl extends GymAbstractDao<Trainer> {

    public TrainerDaoImpl(SessionFactory sessionFactory, UserDao userDao) {
        super(sessionFactory, userDao);
    }

    @Override
    public Trainer update(int id, Trainer updatedTrainer) {
        log.info("Updating trainer with id: " + id);
        Trainer trainer = get(id);
        if (trainer != null) {
            trainer.setUser(updatedTrainer.getUser());
            trainer.setSpecialization(updatedTrainer.getSpecialization());
            try {
                Optional<Trainer> optionalTrainer = Optional.ofNullable(sessionFactory.getCurrentSession().merge(trainer));
                return optionalTrainer.orElseThrow(() -> new InvalidDataException("update(" + id + ", "
                        + updatedTrainer.getUser().getUsername() + " and other fields)",
                        "Check trainer with id: " + id));
            } catch (Exception e) {
                log.error("Error updating trainer with id: " + id, e);
                throw e;
            }
        } else {
            log.error("Trainer with id: " + id + " not found");
            throw new InvalidDataException(Trainer.class.getSimpleName()+"update", "id" + id + "was incorrect");
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
