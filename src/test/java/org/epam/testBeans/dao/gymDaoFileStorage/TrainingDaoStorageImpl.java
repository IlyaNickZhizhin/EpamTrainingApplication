package org.epam.testBeans.dao.gymDaoFileStorage;

import org.epam.model.gymModel.Training;
import org.epam.testBeans.storageInFile.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDaoStorageImpl extends GymDaoStorage<Training> {

    private static final String NAMESPACE = Training.class.getName();

    public TrainingDaoStorageImpl() {
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
