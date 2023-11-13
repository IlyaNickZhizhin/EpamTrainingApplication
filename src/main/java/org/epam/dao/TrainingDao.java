package org.epam.dao;

import org.epam.config.Storage;
import org.epam.model.Model;
import org.epam.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TrainingDao extends TrainDaoStorage<Integer, Training>{

    private Storage storage;
    @Autowired
    public TrainingDao(Storage storage) {
        this.storage = storage;
    }

    Map<Integer, Training> trainings = storage.getTrainings();

    @Override
    public void update(int id, Model model) {
        Training training = (Training) model;
        Training trainingToUpdate = trainings.get(id);
        trainingToUpdate.setTrainingDate(training.getTrainingDate());
        trainingToUpdate.setTrainingName(training.getTrainingName());
        trainingToUpdate.setDuration(training.getDuration());
        trainingToUpdate.setTrainingTypeId(training.getTrainingTypeId());
        trainingToUpdate.setTrainerId(training.getTrainerId());
        trainingToUpdate.setTraineeId(training.getTraineeId());
    }
}
