package org.epam.service;

import org.epam.dao.gymDao.TrainingDao;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TrainingService {
    TrainingDao trainingDao;
    TraineeService traineeService;
    TrainerService trainerService;

    @Autowired
    public TrainingService(TrainingDao trainingDao, TraineeService traineeService, TrainerService trainerService) {
        this.trainingDao = trainingDao;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    public Training create(String Name, Date trainingDate, Number duration, Trainer trainer, Trainee trainee, TrainingType trainingType) {
        Training training = new Training();
        training.setTrainingName(Name);
        training.setTrainingDate(trainingDate);
        training.setDuration(duration);
        training.setTrainerId(trainer.getId());
        training.setTraineeId(trainee.getId());
        training.setTrainingTypeId(trainingType.getId());
        trainingDao.save(training);
        return training;
    }

    public Training select(int id){
        return (Training) trainingDao.get(id);
    }
}
