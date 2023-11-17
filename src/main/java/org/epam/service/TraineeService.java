package org.epam.service;

import org.epam.dao.TraineeDaoImpl;
import org.epam.model.gymModel.Trainee;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeService extends GymAbstractService<Trainee> {

    @Autowired
    public void setTraineeDao(TraineeDaoImpl traineeDao) {
        super.gymDao = traineeDao;
    }

    @Override
    protected Trainee createUserSetter(User user) {
        Trainee trainee = new Trainee();
        trainee.setUser(user);
        return trainee;
    }

    public List<Trainer> updateTrainersList(int id, Trainee traineeForUpdateList) {
        return ((TraineeDaoImpl) super.gymDao).updateTrainersList(id, traineeForUpdateList);
    }

}
