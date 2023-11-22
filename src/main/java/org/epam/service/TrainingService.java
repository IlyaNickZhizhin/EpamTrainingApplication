package org.epam.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.TrainingDaoImpl;
import org.epam.exceptions.InvaildDeveloperException;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.exceptions.ResourceNotFoundException;
import org.epam.exceptions.VerificationException;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@Service
@Transactional
public class TrainingService {

    @Autowired
    TraineeService traineeService;

    @Autowired
    TrainerService trainerService;

    @Autowired
    TrainingDaoImpl trainingDao;

    @Autowired
    PasswordChecker passwordChecker;

    /** This method take parameters of a new Training object and saves it to the database.
     * It logs an informational message before the create operation.
     * @param username (String)
     * @param password (String)
     * @param opponent (UserSetter)
     * @param trainingName (String)
     * @param trainingType (TrainingType)
     * @param trainingDate (LocalDate)
     * @param duration (Number)
     * @return The Training object that was parametrized.
     * @throws VerificationException if username or password is incorrect
     */
    public Training create(String username, String password,
                           Trainee trainee, String trainingName,
                           TrainingType trainingType, LocalDate trainingDate,
                           Double duration) {
        Trainer trainer = trainerService.selectByUsername(username, password);
        Training training = setOtherFields(trainingName, trainingType, trainingDate, duration);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        return trainingDao.create(training);
    }
    public Training create(String username, String password,
                           Trainer trainer, String trainingName,
                           TrainingType trainingType, LocalDate trainingDate,
                           Double duration) {;
        Trainee trainee = traineeService.selectByUsername(username, password);
        Training training = setOtherFields(trainingName, trainingType, trainingDate, duration);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        return trainingDao.create(training);
    }

    private Training setOtherFields(String trainingName, TrainingType trainingType,
                                          LocalDate trainingDate, Double duration){
        Training training = new Training();
        training.setTrainingName(trainingName);
        training.setTrainingType(trainingType);
        training.setTrainingDate(trainingDate);
        training.setDuration(duration);
        return training;
    }

    public List<Trainer> updateTrainersList(int id, Trainee traineeForUpdateList) {
        return trainingDao.updateTrainersList(traineeForUpdateList);
    }

    public List<Trainer> getAllTrainersAvalibleForTrainee(String username, String password) throws InvaildDeveloperException{
        Trainee trainee = traineeService.selectByUsername(username, password);
        List<Trainer> trainers = trainerService.selectAll();
        return trainingDao.getAllTrainersAvalibleForTrainee(trainee, trainers);
    }


    public List<Training> getTrainingsByUsernameAndTrainingTypes (String username, String password,
                                                                          List<TrainingType> types) {
        Trainee trainee = null;
        Trainer trainer = null;
        try {
            trainee = traineeService.selectByUsername(username, password);
        } catch (ResourceNotFoundException e1) {
            try {
            trainer = trainerService.selectByUsername(username, password);
            } catch (ResourceNotFoundException e2) {
                throw new ProhibitedActionException("You are not a trainer or a trainee");
            }
        }
        return trainee != null ? trainingDao.getAllByUsernameAndTrainingTypes(username, types, trainee) :
                trainingDao.getAllByUsernameAndTrainingTypes(username, types, trainer);
    }

}
