package org.epam.service;

import org.epam.dao.TrainerDaoImpl;
import org.epam.exceptions.InvaildDeveloperException;
import org.epam.exceptions.VerificationException;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TrainerService extends GymAbstractService<Trainer> {

    @Autowired
    public void setTrainerDao(TrainerDaoImpl trainerDao) {
        super.gymDao = trainerDao;
    }

    public List<Trainer> getAllTrainersAvalibleForTrainee(Trainee trainee, String username, String password) {
        if (passwordChecker.checkPassword(username, password)) {
            return ((TrainerDaoImpl) super.gymDao).getAllTrainersAvalibleForTrainee(trainee, super.gymDao.getAll());
        }
        throw new InvaildDeveloperException("It is not possible to be here!!!");
    }

    public Trainer create(String firstName, String lastName, TrainingType trainingType) {
        return super.create(firstName, lastName, trainingType);
    }

    @Override
    protected Trainer createModel(Object user, Object... parameters) {
        Trainer trainer = new Trainer();
        trainer.setUser((User) user);
        trainer.setSpecialization((TrainingType) parameters[0]);
        return trainer;
    }

    public Trainer update(String oldUsername, String oldPassword, int id, Trainer upadatedModel) throws VerificationException {
        super.verify(oldUsername, oldPassword);
        return super.update(id, upadatedModel);
    }

    public Trainer select(String username, String password, int id) throws VerificationException {
        super.verify(username, password);
        return super.select(id);
    }

    public Trainer selectByUsername(String username, String password) throws VerificationException {
        super.verify(username, password);
        return super.selectByUsername(username);
    }

    public void changePassword(String username, String oldPassword, String newPassword)throws VerificationException {
        super.verify(username, oldPassword);
        super.changePassword(username, newPassword);
    }


    public void changeActive(String username, String password) throws VerificationException {
        super.verify(username, password);
        super.changeActive(username);
    }

    public void setActive(String username, String password, boolean isActive) throws VerificationException {
        super.verify(username, password);
        super.setActive(username, isActive);
    }
}
