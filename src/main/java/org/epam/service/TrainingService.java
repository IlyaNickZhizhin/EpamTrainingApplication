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

   /* TODO - в сервисах используй аннотацию @RequiredArgsConstructor (вроде такая)
          чтобы не писать руками конструктор каждый раз
   * не стал делать, потому что есть требование "DAO with storage bean should be inserted into services beans
   * using auto wiring. Services beans should be injected into the facade using
   * constructor-based injections. The rest of the injections should be done in a setter-based way."
   * TODO - Здесь решить: можно ли как-то избавиться от явного объявления конструктора?
   * */

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }
    @Autowired
    public void setTraineeService(TraineeService traineeService) {
        this.traineeService = traineeService;
    }
    @Autowired
    public void setTrainerService(TrainerService trainerService) {
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
