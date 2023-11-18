package org.epam.service.storage;

import org.epam.dao.storage.UserDaoStorageImpl;
import org.epam.dao.storage.gymDaoFileStorage.TrainerDaoStorageImpl;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerStorageService {

    TrainerDaoStorageImpl trainerDao;
    UserDaoStorageImpl userDao;


    @Autowired
    public void setTrainerDao(TrainerDaoStorageImpl trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    public void setUserDao(UserDaoStorageImpl userDao) {
        this.userDao = userDao;
    }

    public Trainer create (TrainingType trainingType, String firstName, String lastName) {
        User user = userDao.setNewUser(firstName, lastName);
        Trainer trainer = new Trainer();
        trainer.setSpecialization(trainingType);
        trainer.setUser(userDao.create(user));
        trainerDao.create(trainer);
        return trainer;
    }

    public Trainer update (int id, Trainer trainer){
        trainerDao.update(id, trainer);
        return trainer;
    }

    public Trainer select(int id){
        return trainerDao.get(id);
    }

}
