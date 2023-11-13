package org.epam.dao;

import org.epam.config.Storage;
import org.epam.model.Model;
import org.epam.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TraineeDao extends TrainDaoStorage<Integer, Trainee>{

    Storage storage;

    @Autowired
    public TraineeDao(Storage storage) {
        this.storage = storage;
    }

    Map<Integer, Trainee> trainees = storage.getTrainees();

    @Override
    public void update(int id, Model model) {
        Trainee trainee = (Trainee) model;
        Trainee traineeToUpdate = trainees.get(id);
        traineeToUpdate.setUserId(trainee.getUserId());
        traineeToUpdate.setAddress(trainee.getAddress());
        traineeToUpdate.setAddress(trainee.getAddress());
        traineeToUpdate.setDateOfBirth(trainee.getDateOfBirth());
        save(traineeToUpdate);
    }
}
