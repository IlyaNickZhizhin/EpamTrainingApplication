package org.epam.testBeans.service;

import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.testBeans.dao.UserDaoStorageImpl;
import org.epam.testBeans.dao.gymDaoFileStorage.TraineeDaoStorageImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraineeStorageService {
    TraineeDaoStorageImpl traineeDao;
    UserDaoStorageImpl userDao;

    @Autowired
    public void setTraineeDao(TraineeDaoStorageImpl traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setUserDao(UserDaoStorageImpl userDao) {
        this.userDao = userDao;
    }

    public Trainee create (String firstName, String lastName) {
        User user = userDao.setNewUser(firstName, lastName);
        Trainee trainee = new Trainee();
        trainee.setUser(userDao.create(user));
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
        return traineeDao.get(id);
    }
}
