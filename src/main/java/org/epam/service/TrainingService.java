package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.epam.dao.TrainingDaoImpl;
import org.epam.dao.UserDao;
import org.epam.dto.trainingDto.AddTrainingRequest;
import org.epam.dto.trainingDto.GetTraineeTrainingsListRequest;
import org.epam.dto.trainingDto.GetTrainerTrainingsListRequest;
import org.epam.dto.trainingDto.GetTrainersResponse;
import org.epam.dto.trainingDto.GetTrainingTypesResponse;
import org.epam.dto.trainingDto.GetTrainingsResponse;
import org.epam.dto.trainingDto.UpdateTraineeTrainerListRequest;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.mapper.TrainingMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingService {
    private final UserDao userDao;
    private final TrainingDaoImpl trainingDao;
    private final TrainingMapper trainingMapper;



    @Transactional(readOnly = true)
    public GetTrainingTypesResponse selectAllTrainingTypes(){
        GetTrainingTypesResponse response = new GetTrainingTypesResponse();
        response.setTrainingTypes(trainingDao.getAllTrainingTypes());
        return response;
    }

    @Transactional
    public AddTrainingRequest create(AddTrainingRequest request) {
        User userEE = userDao.getByUsername(request.getTraineeUsername());
        User userER = userDao.getByUsername(request.getTrainerUsername());
        Trainee trainee = userEE.getTrainee();
        Trainer trainer = userER.getTrainer();
        List<Object> eeEr = checkTraineeTrainerConnection(trainee, trainer);
        Training training = new Training();
        training.setTrainee((Trainee) eeEr.get(0));
        training.setTrainer((Trainer) eeEr.get(1));
        training.setTrainingType(trainingMapper.stringToTrainingType(request.getTrainingType()));
        training.setTrainingName(request.getTrainingName());
        training.setTrainingDate(request.getTrainingDate());
        training.setDuration(request.getTrainingDuration());
        return trainingMapper.trainingToAddTrainingRequest(trainingDao.create(training));
    }

    @Transactional
    public GetTrainersResponse updateTrainersList(UpdateTraineeTrainerListRequest request) {
        User user = userDao.getByUsername(request.getTraineeUsername());
        Trainee trainee = user.getTrainee();
        if (trainee == null)
            throw new ProhibitedActionException("Only Trainee could update trainers list");
        List<String> exist = CollectionUtils.emptyIfNull(trainee.getTrainers()).stream()
                .map(trainer -> trainer.getUser().getUsername())
                .collect(Collectors.toList());
        List<String> newTrainers = request.getTrainerUsernames();
        newTrainers.removeAll(exist);
        List<Trainer> trainers = newTrainers.stream()
                .map(userDao::getByUsername)
                .map(User::getTrainer)
                .collect(Collectors.toList());
        trainee.getTrainers().addAll(trainers);
        user.setRole(trainee);
        user = userDao.update(user.getId(), user);
        return trainingMapper.traineeToTrainersResponse(user.getTrainee());
    }

    @Transactional(readOnly = true)
    public GetTrainersResponse getTrainersList(String traineeUsername) {
        User user = userDao.getByUsername(traineeUsername);
        Trainee trainee = user.getTrainee();
        if (trainee == null)
            throw new ProhibitedActionException("Only Trainee could get trainers list");
        return trainingMapper.traineeToTrainersResponse(trainee);
    }

    @Transactional(readOnly = true)
    public GetTrainersResponse getNotAssignedOnTraineeActiveTrainers(String traineeUsername) {
        User user = userDao.getByUsername(traineeUsername);
        Trainee trainee = user.getTrainee();
        if (trainee == null)
            throw new ProhibitedActionException("Only Trainee could get trainers list");
        List<Trainer> existingTrainers = trainee.getTrainers();
        List<Trainer> availableTrainers = trainingDao.getAllTrainersAvalibleForTrainee(
                trainee, existingTrainers);
        GetTrainersResponse response = new GetTrainersResponse();
        response.setTrainers(trainingMapper.trainersToShortTrainersDto(availableTrainers));
        return response;
    }

    @Transactional(readOnly = true)
    public GetTrainingsResponse getTraineeTrainingsList(GetTraineeTrainingsListRequest request) {
        User user = userDao.getByUsername(request.getUsername());
        Trainee trainee = user.getTrainee();
        if (trainee == null)
            throw new ProhibitedActionException("Only Trainee could get trainers list");
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
    public GetTrainingsResponse getTrainerTrainingsList(GetTrainerTrainingsListRequest request) {
        User user = userDao.getByUsername(request.getUsername());
        Trainer trainer = user.getTrainer();
        if (trainer == null)
            throw new ProhibitedActionException("only Trainer could get training list");
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
        if(!trainee.getTrainers().contains(trainer)) {
            trainee.getTrainers().add(trainer);
        }
        if(!trainer.getTrainees().contains(trainee)) {
            trainer.getTrainees().add(trainee);
        }
        userDao.update(trainee.getUser().getId(), trainee.getUser());
        userDao.update(trainer.getUser().getId(), trainer.getUser());
        return List.of(trainee, trainer);
    }

}
