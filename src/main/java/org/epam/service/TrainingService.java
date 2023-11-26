package org.epam.service;

import lombok.extern.slf4j.Slf4j;
import org.epam.dao.TrainingDaoImpl;
import org.epam.dto.LoginRequest;
import org.epam.dto.trainerDto.ShotTrainerDto;
import org.epam.dto.trainerDto.TrainerDto;
import org.epam.dto.trainingDto.AddTrainingRequest;
import org.epam.dto.trainingDto.GetTraineeTrainingsListRequest;
import org.epam.dto.trainingDto.GetTrainerTrainingsListRequest;
import org.epam.dto.trainingDto.GetTrainersResponse;
import org.epam.dto.trainingDto.GetTrainingTypesResponse;
import org.epam.dto.trainingDto.GetTrainingsResponse;
import org.epam.dto.trainingDto.UpdateTraineeTrainerListRequest;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.exceptions.VerificationException;
import org.epam.mapper.GymGeneralMapper;
import org.epam.mapper.TrainingMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class TrainingService {

    @Autowired
    TraineeService traineeService;

    @Autowired
    TrainerService trainerService;

    @Autowired
    TrainingDaoImpl trainingDao;

    @Autowired
    TrainingMapper trainingMapper;

    @Autowired
    GymGeneralMapper gymGeneralMapper;


    @Transactional(readOnly = true)
    public GetTrainingTypesResponse selectAllTrainingTypes(LoginRequest login){
        try {
            trainerService.loginAndTakeModel(login);
        } catch (ProhibitedActionException | VerificationException e1) {
            try {
            traineeService.loginAndTakeModel(login);
            } catch (ProhibitedActionException | VerificationException e2) {
                throw new VerificationException("Incorrect password for training creating" + e1.getMessage() + e2.getMessage());
            }
        }
        GetTrainingTypesResponse response = new GetTrainingTypesResponse();
        response.setTrainingTypes(trainingDao.getAllTrainingTypes());
        return response;
    }

    @Transactional
    public void create(LoginRequest login, AddTrainingRequest request) {
        Trainer trainer = null;
        Trainee trainee = null;
        try {
            trainer = trainerService.loginAndTakeModel(login);
        } catch (ProhibitedActionException | VerificationException e1) {
            try {
                trainee = traineeService.loginAndTakeModel(login);
            } catch (ProhibitedActionException | VerificationException e2) {
                throw new VerificationException("Incorrect password for training creating: " + e1.getMessage() + e2.getMessage());
            }
        }
        if (trainer == null) {
           trainer = trainerService.selectByUsername(request.getTrainerUsername());
        } else {
            trainee = traineeService.selectByUsername(request.getTraineeUsername());
        }
        Training training = new Training();
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingType(gymGeneralMapper.stringToTrainingType(request.getTrainingType()));
        training.setTrainingName(request.getTrainingName());
        training.setTrainingDate(request.getTrainingDate());
        training.setDuration(request.getTrainingDuration());
        trainingDao.create(training);
    }

    @Transactional
    public GetTrainersResponse updateTrainersList(LoginRequest login, String traineeUsername, UpdateTraineeTrainerListRequest request) {
        Trainee trainee = traineeService.loginAndTakeModel(login); // password cheking
        List<String> exist = trainee.getTrainings().stream()
                .map(Training::getTrainer).map(Trainer::getUser).map(User::getUsername).collect(Collectors.toList());
        List<String> newTrainers = request.getTrainerUsernames();
        newTrainers.removeAll(exist);
        List<Trainer> trainers = newTrainers.stream().map(trainerService::selectByUsername).collect(Collectors.toList());
        for (Trainer trainer:
             trainers) {
            Training training = new Training();
            training.setTrainer(trainer);
            training.setTrainee(trainee);
            training.setTrainingType(trainer.getSpecialization());
            training.setTrainingName(
                    trainer.getSpecialization().getTrainingName().toString()
                    + " with trainer "
                    + trainer.getUser().getFirstName());
            training.setTrainingDate(LocalDate.now());
            training.setDuration(1.0);
            trainingDao.create(training);
        }
        return getTrainersList(login, traineeUsername);
    }

    @Transactional(readOnly = true)
    public GetTrainersResponse getTrainersList(LoginRequest login, String traineeUsername) {
        Trainee trainee = traineeService.loginAndTakeModel(login); // password cheking
        List<Trainer> trainers = trainee.getTrainings().stream().map(Training::getTrainer).collect(Collectors.toList());
        List<ShotTrainerDto> shotTrainers = gymGeneralMapper.trainersDtoToShot(
                gymGeneralMapper.trainersToTrainerDtos(trainers));
        GetTrainersResponse response = new GetTrainersResponse();
        response.setTrainers(shotTrainers);
        return response;
    }

    @Transactional(readOnly = true)
    public GetTrainersResponse getNotAssignedOnTraineeActiveTrainers(LoginRequest login, String traineeUsername) {
        Trainee trainee = traineeService.loginAndTakeModel(login); // password cheking
        List<Trainer> existingTrainers = trainee.getTrainings().stream().map(Training::getTrainer).collect(Collectors.toList());
        List<Trainer> avalibleTrainers = trainingDao.getAllTrainersAvalibleForTrainee(
                trainee, existingTrainers);
        List<TrainerDto> trainers= gymGeneralMapper.trainersToTrainerDtos(avalibleTrainers);
        List<ShotTrainerDto> shotTrainers = gymGeneralMapper.trainersDtoToShot(trainers);
        GetTrainersResponse response = new GetTrainersResponse();
        response.setTrainers(shotTrainers);
        return response;
    }

    @Transactional(readOnly = true)
    public GetTrainingsResponse getTraineeTrainingsList(LoginRequest login, GetTraineeTrainingsListRequest request) {
        Trainee trainee = traineeService.loginAndTakeModel(login); // password cheking
        List<Training> trainings = trainee.getTrainings();
        if (request.getPeriodFrom() != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainingDate().isAfter(request.getPeriodFrom()))
                    .collect(Collectors.toList());
        }
        if (request.getPeriodTo() != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainingDate().isBefore(request.getPeriodTo()))
                    .collect(Collectors.toList());
        }
        if (request.getTrainingType() != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainingType().equals(TrainingType.of(request.getTrainingType())))
                    .collect(Collectors.toList());
        }
        if (request.getTrainerName() != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainer().getUser().getUsername().equals(request.getTrainerName()))
                    .collect(Collectors.toList());
        }
        GetTrainingsResponse response = new GetTrainingsResponse();
        response.setTrainings(trainingMapper.toShotTrainingDtoListForTrainee(
                gymGeneralMapper.trainingsToTrainingDtos(trainings)));
        return response;
    }

    @Transactional(readOnly = true)
    public GetTrainingsResponse getTrainerTrainingsList(LoginRequest login, GetTrainerTrainingsListRequest request) {
        Trainer trainer = trainerService.loginAndTakeModel(login); // password cheking
        List<Training> trainings = trainer.getTrainings();
        if (request.getPeriodFrom() != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainingDate().isAfter(request.getPeriodFrom()))
                    .collect(Collectors.toList());
        }
        if (request.getPeriodTo() != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainingDate().isBefore(request.getPeriodTo()))
                    .collect(Collectors.toList());
        }
        if (request.getTraineeName() != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainee().getUser().getUsername().equals(request.getTraineeName()))
                    .collect(Collectors.toList());
        }
        GetTrainingsResponse response = new GetTrainingsResponse();
        response.setTrainings(trainingMapper.toShotTrainingDtoListForTrainer(
                gymGeneralMapper.trainingsToTrainingDtos(trainings)));
        return response;
    }

}
