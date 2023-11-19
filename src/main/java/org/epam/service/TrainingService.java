package org.epam.service;

import org.epam.dao.TraineeDaoImpl;
import org.epam.dao.TrainerDaoImpl;
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
    public List<Trainer> updateTrainersList(int id, Trainee traineeForUpdateList) {
        return ((TrainingDaoImpl) super.gymDao).updateTrainersList(id, traineeForUpdateList);
    }

    public List<Trainer> getAllTrainersAvalibleForTrainee(Trainee trainee, String username, String password) {
        if (passwordChecker.checkPassword(username, password)) {
            return ((TrainingDaoImpl) super.gymDao).getAllTrainersAvalibleForTrainee(trainee, trainerService.selectAll());
        }
        throw new InvaildDeveloperException("It is not possible to be here!!!");
    }

    public List<Training> getTrainingsByTrainerAndTrainingTypesForTrainer(
            List<TrainingType> types, String trainerUsername, String trainerPassword) throws VerificationException {
        super.verify(trainerUsername, trainerPassword);
        Trainer trainer = trainerService.selectByUsername(trainerUsername, trainerPassword);
        return TRAINERgetTrainingsByTrainerAndTrainingTypesPRIVATE(trainer, types);
    }

    public List<Training> getTrainingsByTraineeAndTrainingTypesForTrainee(
            List<TrainingType> types, String traineeUsername, String traineePassword) {
        Trainee trainee = traineeService.selectByUsername(traineeUsername, traineePassword);
        return ((TrainingDaoImpl) gymDao).getAllByTraineeAndTrainingTypes(trainee, types);
    }

    public Training create(String username, String password,
                           UserSetter opponent, String trainingName,
                           TrainingType trainingType, LocalDate trainingDate,
                           Number duration) {
        return super.create(username, password, opponent, trainingName, trainingType, trainingDate, duration);
    }

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

    private List<Training> TRAINERgetTrainingsByTrainerAndTrainingTypesPRIVATE(Trainer trainer,
                                                                               List<TrainingType> types) {
            return ((TrainingDaoImpl) gymDao).getAllByTrainerAndTrainingTypes(trainer, types);
    }

    private List<Training> TRAINEEgetTrainingsByTraineeAndTrainingTypesPRIVATE (Trainee trainee,
                                                                                List<TrainingType> types) {
        return ((TrainingDaoImpl) gymDao).getAllByTraineeAndTrainingTypes(trainee, types);
    }
}
