package org.epam.service.storage;

import org.epam.dao.storage.gymDaoFileStorage.TrainingDaoStorageImpl;
import org.epam.dao.storage.gymDaoFileStorage.TraineeDaoStorageImpl;
import org.epam.service.storage.TrainerStorageService;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TrainingStorageService {
    TrainingDaoStorageImpl trainingDao;
    TraineeStorageService traineeService;
    TrainerStorageService trainerService;

    @Autowired
    public void setTrainingDao(TrainingDaoStorageImpl trainingDao) {
        this.trainingDao = trainingDao;
    }
    @Autowired
    public void setTraineeService(TraineeStorageService traineeService) {
        this.traineeService = traineeService;
    }
    @Autowired
    public void setTrainerService(TrainerStorageService trainerService) {
        this.trainerService = trainerService;
    }



    public Training create(String Name, Date trainingDate, Number duration, Trainer trainer, Trainee trainee, TrainingType trainingType) {
        Training training = new Training();
        training.setTrainingName(Name);
        training.setTrainingDate(trainingDate);
        training.setDuration(duration);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingType(trainingType);
        trainingDao.save(training);
        return training;
    }

    public Training select(int id){
        return trainingDao.get(id);
    }
}
