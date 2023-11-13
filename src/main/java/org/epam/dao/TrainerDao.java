package org.epam.dao;

import org.epam.config.Storage;
import org.epam.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class TrainerDao implements Dao<Trainer> {

    Storage storage;

    @Autowired
    public TrainerDao(Storage storage) {
        this.storage = storage;
    }

    private static final AtomicInteger AUTO_ID = new AtomicInteger(0);
    Map<Integer, Trainer> trainers = storage.getTrainers();

    @Override
    public void save(Trainer trainer) {
        trainers.put(AUTO_ID.getAndIncrement(), trainer);
    }

    @Override
    public void update(int id, Trainer trainer) {
        Trainer trainerToUpdate = trainers.get(id);
        trainerToUpdate.setUserId(trainer.getUserId());
        trainerToUpdate.setSpecialization(trainer.getSpecialization());
        save(trainerToUpdate);
    }

    @Override
    public void delete(int id) {
        trainers.remove(id);
    }

    @Override
    public Trainer get(int id) {
        return trainers.get(id);
    }
}