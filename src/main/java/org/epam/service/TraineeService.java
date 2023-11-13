package org.epam.service;

import org.epam.dao.TraineeDao;
import org.epam.dao.UserDao;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.TrainingType;
import org.epam.model.User;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TraineeService {
    TraineeDao traineeDao;
    UserDao userDao;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public Trainee create (String firstName, String lastName) {
        User user = userDao.setNewUser(firstName, lastName);
        Trainee trainee = new Trainee();
        trainee.setUserId(userDao.save(user));
        traineeDao.save(trainee);
        return trainee;
    }

    public Trainee update (int id, Trainee trainee){
        traineeDao.update(id, trainee);
        return trainee;
    }

    public void delete (int id){
        traineeDao.delete(id);
    }

    public void select(int id){
        traineeDao.get(id);
    }
}
