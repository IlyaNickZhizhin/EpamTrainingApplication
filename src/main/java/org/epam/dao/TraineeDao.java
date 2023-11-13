package org.epam.dao;

import org.epam.config.Storage;
import org.epam.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class TraineeDao implements Dao<Trainee>{

    Storage storage;

    @Autowired
    public TraineeDao(Storage storage) {
        this.storage = storage;
    }

    private static final AtomicInteger AUTO_ID = new AtomicInteger(0);
    Map<Integer, Trainee> trainees = storage.getTrainees();

    @Override
    public void save(Trainee trainee) {
        trainees.put(AUTO_ID.getAndIncrement(), trainee);
    }

    @Override
    public void update(int id, Trainee trainee) {
        Trainee traineeToUpdate = trainees.get(id);
        traineeToUpdate.setUserId(trainee.getUserId());
        traineeToUpdate.setAddress(trainee.getAddress());
        traineeToUpdate.setAddress(trainee.getAddress());
        traineeToUpdate.setDateOfBirth(trainee.getDateOfBirth());
        save(traineeToUpdate);
    }

    @Override
    public void delete(int id) {
        trainees.remove(id);
    }

    @Override
    public Trainee get(int id) {
        return trainees.get(id);
    }
}
