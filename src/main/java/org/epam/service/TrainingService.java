package org.epam.service;

import org.epam.dao.TrainingDaoImpl;
import org.epam.exceptions.InvaildDeveloperException;
import org.epam.exceptions.ProhibitedAction;
import org.epam.exceptions.VerificationException;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.epam.model.gymModel.UserSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * This class is the Service for Training models.
 * @see org.epam.model.gymModel.Training
 * @see org.epam.service.GymAbstractService
 * @see org.epam.dao.TrainingDaoImpl
 * @see org.epam.service.TrainingService#create(String, String, UserSetter, String, TrainingType, LocalDate, Number)
 * @see org.epam.service.TrainingService#createModel(Object, Object...)
 * @see org.epam.service.TrainingService#updateTrainersList(int, Trainee)
 * @see org.epam.service.TrainingService#getAllTrainersAvalibleForTrainee(Trainee, String, String)
 * @see org.epam.service.TrainingService#getTrainingsByTrainerAndTrainingTypesForTrainer(List, String, String)
 * @see org.epam.service.TrainingService#getTrainingsByTraineeAndTrainingTypesForTrainee(List, String, String)
 */
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

    //TODO я не понял зачем этот метод и сделал его НЕ верно.

    /**
     * This method updates a Trainer in the database using its ID and an updated Trainer object.
     * It logs an informational message before the update operation.
     * @param id (int)
     * @param traineeForUpdateList (Trainee)
     * @return List<Trainer>
     */
    public List<Trainer> updateTrainersList(int id, Trainee traineeForUpdateList) {
        return ((TrainingDaoImpl) super.gymDao).updateTrainersList(id, traineeForUpdateList);
    }

    /**
     * This method returns all trainers available for trainee.
     * @param trainee (Trainee)
     * @param username (String)
     * @param password (String)
     * @return List<Trainer>
     */
    public List<Trainer> getAllTrainersAvalibleForTrainee(Trainee trainee, String username, String password) {
        if (passwordChecker.checkPassword(username, password)) {
            return ((TrainingDaoImpl) super.gymDao).getAllTrainersAvalibleForTrainee(trainee, trainerService.selectAll());
        }
        throw new InvaildDeveloperException("It is not possible to be here!!!");
    }

    /**
     * This method returns all trainings for trainer by trainer and training types.
     * @param types (List<TrainingType>)
     * @param trainerUsername (String)
     * @param trainerPassword (String)
     * @return List<Training>
     * @throws VerificationException if username or password is incorrect
     */
    public List<Training> getTrainingsByTrainerAndTrainingTypesForTrainer(
            String trainerUsername, String trainerPassword, List<TrainingType> types) throws VerificationException {
        super.verify(trainerUsername, trainerPassword);
        Trainer trainer = trainerService.selectByUsername(trainerUsername, trainerPassword);
        return TRAINERgetTrainingsByTrainerAndTrainingTypesPRIVATE(trainer, types);
    }

    /**
     * This method returns all trainings for trainee by trainee and training types.
     * @param types (List<TrainingType>)
     * @param traineeUsername (String)
     * @param traineePassword (String)
     * @return List<Training>
     * @throws VerificationException if username or password is incorrect
     */
    public List<Training> getTrainingsByTraineeAndTrainingTypesForTrainee(
            List<TrainingType> types, String traineeUsername, String traineePassword) {
        Trainee trainee = traineeService.selectByUsername(traineeUsername, traineePassword);
        return ((TrainingDaoImpl) gymDao).getAllByTraineeAndTrainingTypes(trainee, types);
    }

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
                           UserSetter opponent, String trainingName,
                           TrainingType trainingType, LocalDate trainingDate,
                           Number duration) {
        return super.create(username, password, opponent, trainingName, trainingType, trainingDate, duration);
    }

    /** This method parametrize a new Training object before saves it to the database.
     * It logs an informational message before the create operation.
     * @param who (Object)
     * @param parameters (Object...)
     * @return The Training object that was created.
     */
    @Override
    protected Training createModel(Object who, Object... parameters) {
        Training training = new Training();
        List<Training> existing;
        Trainee ee;
        Trainer er;
        String ingName = (String) parameters[1];
        TrainingType type = (TrainingType) parameters[2];
        LocalDate date = (LocalDate) parameters[3];
        Number duration = (Number) parameters[4];
        if (who.getClass().equals(Trainee.class)) {
            ee = (Trainee) who;
            er = (Trainer) parameters[0];
            existing = TRAINEEgetTrainingsByTraineeAndTrainingTypesPRIVATE(ee, List.of(type));
        } else {
            er = (Trainer) who;
            ee = (Trainee) parameters[0];
            existing = TRAINERgetTrainingsByTrainerAndTrainingTypesPRIVATE(er, List.of(type));
        }
        long numOfTrainings = existing.stream()
                .filter(tr -> tr.getTrainingDate().equals(date))
                .filter(tr -> tr.getDuration().equals(duration))
                .filter(tr -> tr.getTrainingName().equals(ingName)).count();
        if (numOfTrainings > 0) {
            throw new ProhibitedAction("This training already exists");
        }
        training.setTrainer(er);
        training.setTrainee(ee);
        training.setTrainingName(ingName);
        training.setTrainingType(type);
        training.setTrainingDate(date);
        training.setDuration(duration);
        return training;
    }

    /**
     * This method returns all trainings for trainer by trainer and training types.
     * @param trainer (Trainer)
     * @param types (List<TrainingType>)
     * @return List<Training>
     */
    private List<Training> TRAINERgetTrainingsByTrainerAndTrainingTypesPRIVATE(Trainer trainer,
                                                                               List<TrainingType> types) {
            return ((TrainingDaoImpl) gymDao).getAllByTrainerAndTrainingTypes(trainer, types);
    }

    /**
     *  This method returns all trainings for trainee by trainee and training types.
     * @param trainee (Trainee)
     * @param types (List<TrainingType>)
     * @return List<Training>
     */
    private List<Training> TRAINEEgetTrainingsByTraineeAndTrainingTypesPRIVATE (Trainee trainee,
                                                                                List<TrainingType> types) {
        return ((TrainingDaoImpl) gymDao).getAllByTraineeAndTrainingTypes(trainee, types);
    }
}
