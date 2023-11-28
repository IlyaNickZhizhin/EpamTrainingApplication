package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.epam.dao.TrainingDaoImpl;
import org.epam.dto.LoginRequest;
import org.epam.dto.trainingDto.AddTrainingRequest;
import org.epam.dto.trainingDto.GetTraineeTrainingsListRequest;
import org.epam.dto.trainingDto.GetTrainerTrainingsListRequest;
import org.epam.dto.trainingDto.GetTrainersResponse;
import org.epam.dto.trainingDto.GetTrainingTypesResponse;
import org.epam.dto.trainingDto.GetTrainingsResponse;
import org.epam.dto.trainingDto.UpdateTraineeTrainerListRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingService {
    private final GymGeneralService<Trainee> traineeService;
    private final GymGeneralService<Trainer> trainerService;
    private final TrainingDaoImpl trainingDao;
    private final TrainingMapper trainingMapper = TrainingMapper.INSTANCE;



    @Transactional(readOnly = true)
    public GetTrainingTypesResponse selectAllTrainingTypes(){
        GetTrainingTypesResponse response = new GetTrainingTypesResponse();
        response.setTrainingTypes(trainingDao.getAllTrainingTypes());
        return response;
    }

    @Transactional
    public void create(AddTrainingRequest request) {
        Trainer trainer = trainerService.selectByUsername(request.getTrainerUsername());
        Trainee trainee = traineeService.selectByUsername(request.getTraineeUsername());
        List<Object> eeEr = checkTraineeTrainerConnection(trainee, trainer);
        Training training = new Training();
        training.setTrainee((Trainee) eeEr.get(0));
        training.setTrainer((Trainer) eeEr.get(1));
        training.setTrainingType(trainingMapper.stringToTrainingType(request.getTrainingType()));
        training.setTrainingName(request.getTrainingName());
        training.setTrainingDate(request.getTrainingDate());
        training.setDuration(request.getTrainingDuration());
        trainingDao.create(training);
    }

    @Transactional
    public GetTrainersResponse updateTrainersList(LoginRequest login, String traineeUsername, UpdateTraineeTrainerListRequest request) {
        Trainee trainee = traineeService.selectByUsername(request.getTraineeUsername());
        List<String> exist = trainee.getTrainings().stream()
                .map(Training::getTrainer).map(Trainer::getUser).map(User::getUsername).collect(Collectors.toList());
        List<String> newTrainers = request.getTrainerUsernames();
        newTrainers.removeAll(exist);
        List<Trainer> trainers = newTrainers.stream().map(trainerService::selectByUsername).collect(Collectors.toList());
        org.apache.commons.collections4.CollectionUtils.emptyIfNull(trainee.getTrainers()).addAll(trainers);
        traineeService.update(trainee.getId(), trainee);
        return getTrainersList(traineeUsername);
    }

    @Transactional(readOnly = true)
    public GetTrainersResponse getTrainersList(String traineeUsername) {
        Trainee trainee = traineeService.selectByUsername(traineeUsername);
        return trainingMapper.traineeToTrainersResponse(trainee);
    }

    @Transactional(readOnly = true)
    public GetTrainersResponse getNotAssignedOnTraineeActiveTrainers(String traineeUsername) {
        Trainee trainee = traineeService.selectByUsername(traineeUsername);
        List<Trainer> existingTrainers = new ArrayList<>(CollectionUtils.emptyIfNull(trainee.getTrainers()));
        List<Trainer> avalibleTrainers = trainingDao.getAllTrainersAvalibleForTrainee(
                trainee, existingTrainers);
        GetTrainersResponse response = new GetTrainersResponse();
        response.setTrainers(trainingMapper.trainersToShortTrainersDto(avalibleTrainers));
        return response;
    }

    @Transactional(readOnly = true)
    public GetTrainingsResponse getTraineeTrainingsList(GetTraineeTrainingsListRequest request) {
        Trainee trainee = traineeService.selectByUsername(request.getUsername());
        List<Training> trainings = trainingFilterByDate(trainee.getTrainings(), request.getPeriodFrom(), request.getPeriodTo());
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
        response.setTrainings(trainingMapper.traineeTrainingsToShortDtos(trainings));
        return response;
    }

    @Transactional(readOnly = true)
    public GetTrainingsResponse getTrainerTrainingsList(LoginRequest login, GetTrainerTrainingsListRequest request) {
        Trainer trainer = trainerService.selectByUsername(request.getUsername());
        List<Training> trainings = trainingFilterByDate(trainer.getTrainings(), request.getPeriodFrom(), request.getPeriodTo());
        if (request.getTraineeName() != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainee().getUser().getUsername().equals(request.getTraineeName()))
                    .collect(Collectors.toList());
        }
        GetTrainingsResponse response = new GetTrainingsResponse();
        response.setTrainings(trainingMapper.trainerTrainingsToShortDtos(trainings));
        return response;
    }

    private List<Training> trainingFilterByDate(List<Training> trainings, LocalDate periodFrom, LocalDate periodTo) {
        if (periodFrom != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainingDate().isAfter(periodFrom))
                    .collect(Collectors.toList());
        }
        if (periodTo != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainingDate().isBefore(periodTo))
                    .collect(Collectors.toList());
        }
        return trainings;
    }

    private List<Object> checkTraineeTrainerConnection(Trainee trainee, Trainer trainer){
        if(!CollectionUtils.emptyIfNull(trainee.getTrainers()).contains(trainer)) {
            trainee.getTrainers().add(trainer);
        }
        if(!CollectionUtils.emptyIfNull(trainer.getTrainees()).contains(trainee)) {
            trainer.getTrainees().add(trainee);
        }
        traineeService.update(trainee.getId(), trainee);
        trainerService.update(trainer.getId(), trainer);
        return List.of(trainee, trainer);
    }

}
