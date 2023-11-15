package org.epam.dao.gymDao;

import org.epam.storageInFile.Storage;
import org.epam.model.gymModel.Model;
import org.epam.model.gymModel.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDao extends GymDaoStorage<Training> {

    private static final String NAMESPACE = Training.class.getName();
    @Autowired
    public TrainingDao(Storage storage) {
        super(storage);
        super.namespace = NAMESPACE;
    }

    @Override
    public void update(int id, Training training) {
        Training trainingToUpdate = storage.getGymModels().get(NAMESPACE).get(id);
        trainingToUpdate.setTrainingDate(training.getTrainingDate());
        trainingToUpdate.setTrainingName(training.getTrainingName());
        trainingToUpdate.setDuration(training.getDuration());
        trainingToUpdate.setTrainingTypeId(training.getTrainingTypeId());
        trainingToUpdate.setTrainerId(training.getTrainerId());
        trainingToUpdate.setTraineeId(training.getTraineeId());
        save(trainingToUpdate);
    }
}
