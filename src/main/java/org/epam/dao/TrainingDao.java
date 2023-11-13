package org.epam.dao;

import org.epam.config.Storage;
import org.epam.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
@Repository
public class TrainingDao implements Dao<Training>{

    private Storage storage;
    @Autowired
    public TrainingDao(Storage storage) {
        this.storage = storage;
    }

    Map<Integer, Training> trainings = storage.getTrainings();

    @Override
    public void save(Training training) {
        trainings.put(training.getId(), training);
    }

    @Override
    public void update(int id, Training training) {
        Training trainingToUpdate = trainings.get(id);
        trainingToUpdate.setTrainingDate(training.getTrainingDate());
        trainingToUpdate.setTrainingName(training.getTrainingName());
        trainingToUpdate.setDuration(training.getDuration());
        trainingToUpdate.setTrainingTypeId(training.getTrainingTypeId());
        trainingToUpdate.setTrainerId(training.getTrainerId());
        trainingToUpdate.setTraineeId(training.getTraineeId());
    }

    @Override
    public void delete(int id) {
        trainings.remove(id);
    }

    @Override
    public Training get(int id) {
        return trainings.get(id);
    }
}
