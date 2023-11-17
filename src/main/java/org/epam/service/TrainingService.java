package org.epam.service;

import org.epam.dao.TrainingDaoImpl;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;

@Service
public class TrainingService extends GymAbstractService<Training> {

    TraineeService traineeService;
    TrainerService trainerService;

    @Autowired
    public void setTrainingDao(TrainingDaoImpl trainingDao) {
        super.gymDao = trainingDao;
    }
    @Autowired
    public void setTraineeService(TraineeService traineeService) {
        this.traineeService = traineeService;
    }
    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    public Training createByTrainer(String Name, Date trainingDate, Number duration, Trainer trainer,
                                    Trainee trainee, TrainingType trainingType,
                                    String trainerUsername, String trainerPassword) throws AccessDeniedException {
        if (super.passwordChecker.checkPassword(trainerUsername, trainerPassword)) {
            return create(Name, trainingDate, duration, trainer, trainee, trainingType);
        }
        throw new AccessDeniedException("Wrong password");
    }

    public Training createByTrainee(String Name, Date trainingDate, Number duration, Trainer trainer,
                                    Trainee trainee, TrainingType trainingType,
                                    String traineeUsername, String traineePassword) throws AccessDeniedException {
        if (super.passwordChecker.checkPassword(traineeUsername, traineePassword)) {
            return create(Name, trainingDate, duration, trainer, trainee, trainingType);
        }
        throw new AccessDeniedException("Wrong password");
    }

    public Training create(String Name, Date trainingDate, Number duration, Trainer trainer, Trainee trainee, TrainingType trainingType) {
        Training training = new Training();
        training.setTrainingName(Name);
        training.setTrainingDate(trainingDate);
        training.setDuration(duration);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingType(trainingType);
        gymDao.save(training);
        return training;
    }

    public List<Training> getTrainingsByTraineeAndTrainingTypesForTrainer(Trainee trainee, List<TrainingType> types,
                                                                          String trainerUsername, String trainerPassword)
            throws AccessDeniedException {
        if (super.passwordChecker.checkPassword(trainerUsername, trainerPassword)) {
            return ((TrainingDaoImpl) gymDao).getAllByTraineeAndTrainingTypes(trainee, types);
        }
        throw new AccessDeniedException("Wrong password");
    }

    public List<Training> getTrainingsByTraineeAndTrainingTypesForTrainee(
            List<TrainingType> types, String traineeUsername, String traineePassword) throws AccessDeniedException {
        Trainee trainee = traineeService.selectByUsername(traineeUsername, traineePassword);
        return ((TrainingDaoImpl) gymDao).getAllByTraineeAndTrainingTypes(trainee, types);
    }

    @Override
    public Training update(int id, Training upadatedModel, String oldUsername, String oldPassword) {
        throw new UnsupportedOperationException("No it is not possible to update training");
    }

    @Override
    public void delete(int id, String username, String password) {
        throw new UnsupportedOperationException("No it is not possible to delete training");
    }

    @Override
    protected Training createUserSetter(User user) {
        throw new UnsupportedOperationException("No it is not possible to create user setter for training");
    }

    @Override
    public Training selectByUsername(String username, String password) {
        throw new UnsupportedOperationException("No it is not possible to select training by username");
    }

    @Override
    public void changePassword(String username, String password) {
        throw new UnsupportedOperationException("No it is not possible to select training by username");
    }
}
