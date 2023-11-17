package org.epam.service;

import org.epam.dao.TrainerDaoImpl;
import org.epam.exceptions.InvaildDeveloperException;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.TrainingType;
import org.epam.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class TrainerService extends GymAbstractService<Trainer> {

    @Autowired
    public void setTrainerDao(TrainerDaoImpl trainerDao) {
        super.gymDao = trainerDao;
    }


    public Trainer create(TrainingType trainingType, String firstName, String lastName) {
        Trainer trainer = super.create(firstName, lastName);
        trainer.setSpecialization(trainingType);
        try {
            return super.update(trainer.getId(), trainer, trainer.getUser().getUsername(), trainer.getUser().getPassword());
        } catch (AccessDeniedException e) {
            throw new InvaildDeveloperException("TrainerService.create() method failed");
        }
    }

    public List<Trainer> getAllTrainersAvalibleForTrainee(Trainee trainee, String username, String password) throws AccessDeniedException {
        if (passwordChecker.checkPassword(username, password)) {
            return ((TrainerDaoImpl) super.gymDao).getAllTrainersAvalibleForTrainee(trainee, super.gymDao.getAll());
        }
        throw new AccessDeniedException("Wrong password");
    }

    public void delete(int id) {
        throw new UnsupportedOperationException("This method was prohibited according to the task");
    }

    @Override
    protected Trainer createUserSetter(User user) {
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        return trainer;
    }

}
