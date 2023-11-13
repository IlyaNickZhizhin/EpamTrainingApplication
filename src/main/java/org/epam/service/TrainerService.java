package org.epam.service;

import org.epam.dao.TrainerDao;
import org.epam.dao.UserDao;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.TrainingType;
import org.epam.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerService {

    TrainerDao trainerDao;
    UserDao userDao;

    @Autowired
    public TrainerService(TrainerDao trainerDao, UserDao userDao) {
        this.trainerDao = trainerDao;
        this.userDao = userDao;
    }

    public Trainer create (TrainingType trainingType, String firstName, String lastName) {
        User user = userDao.setNewUser(firstName, lastName);
        Trainer trainer = new Trainer();
        trainer.setSpecialization(trainingType.getName());
        trainer.setUserId(userDao.save(user));
        trainerDao.save(trainer);
        return trainer;
    }

    public Trainer update (int id, Trainer trainer){
        trainerDao.update(id, trainer);
        return trainer;
    }

    public void select(int id){
        trainerDao.get(id);
    }

}
