package org.epam.dao.gymDao;

import org.epam.config.Storage;
import org.epam.model.gymModel.Model;
import org.epam.model.gymModel.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TrainingDao extends GymDaoStorage<Integer, Training> {

    private static final String NAMESPACE = "trainings";
    @Autowired
    public TrainingDao(Storage storage) {
        super(storage);
        super.namespace = NAMESPACE;
    }

    Map<Integer, Training> trainings = storage.getTrainings();

    @Override
    public void update(int id, Model model) {
        Training training = (Training) model;
        Training trainingToUpdate = (Training) models.get(NAMESPACE).get(id);
        trainingToUpdate.setTrainingDate(training.getTrainingDate());
        trainingToUpdate.setTrainingName(training.getTrainingName());
        trainingToUpdate.setDuration(training.getDuration());
        trainingToUpdate.setTrainingTypeId(training.getTrainingTypeId());
        trainingToUpdate.setTrainerId(training.getTrainerId());
        trainingToUpdate.setTraineeId(training.getTraineeId());
        save(trainingToUpdate);
    }
}