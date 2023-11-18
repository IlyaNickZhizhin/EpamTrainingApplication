package org.epam.dao.storage.gymDaoFileStorage;

import org.epam.model.gymModel.Training;
import org.epam.storageInFile.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDaoStorageImpl extends GymDaoStorage<Training> {

    private static final String NAMESPACE = Training.class.getName();
    @Autowired
    public TrainingDaoStorageImpl(Storage<Training> storage) {
        super(storage);
        super.namespace = NAMESPACE;
    }

    @Override
    public void update(int id, Training training) {
        Training trainingToUpdate = storage.getGymModels().get(NAMESPACE).get(id);
        trainingToUpdate.setTrainingDate(training.getTrainingDate());
        trainingToUpdate.setTrainingName(training.getTrainingName());
        trainingToUpdate.setDuration(training.getDuration());
        trainingToUpdate.setTrainingType(training.getTrainingType());
        trainingToUpdate.setTrainer(training.getTrainer());
        trainingToUpdate.setTrainee(training.getTrainee());
        save(trainingToUpdate);
    }
}