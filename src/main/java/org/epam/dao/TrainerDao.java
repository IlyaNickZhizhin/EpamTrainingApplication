package org.epam.dao;

import org.epam.config.Storage;
import org.epam.model.Model;
import org.epam.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TrainerDao extends TrainDaoStorage<Integer, Trainer> {

    Storage storage;

    @Autowired
    public TrainerDao(Storage storage) {
        this.storage = storage;
    }

    Map<Integer, Trainer> trainers = storage.getTrainers();

    @Override
    public void update(int id, Model model) {
        Trainer trainer = (Trainer) model;
        Trainer trainerToUpdate = trainers.get(id);
        trainerToUpdate.setUserId(trainer.getUserId());
        trainerToUpdate.setSpecialization(trainer.getSpecialization());
        save(trainerToUpdate);
    }

}