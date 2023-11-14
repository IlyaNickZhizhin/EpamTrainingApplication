package org.epam.service;

import org.epam.dao.gymDao.TraineeDao;
import org.epam.dao.UserDao;
import org.epam.model.gymModel.Trainee;
import org.epam.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        trainee.setUserId(userDao.create(user));
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

    public Trainee select(int id){
        return (Trainee) traineeDao.get(id);
    }
}
