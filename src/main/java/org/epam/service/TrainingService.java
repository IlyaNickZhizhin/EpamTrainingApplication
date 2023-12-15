package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.epam.dao.TraineeRepository;
import org.epam.dao.TrainerRepository;
import org.epam.dao.TrainingRepository;
import org.epam.dao.UserRepository;
import org.epam.dto.trainingDto.*;
import org.epam.exceptions.InvalidDataException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingService {
    private final UserRepository userDao;
    private final TraineeRepository traineeDao;
    private final TrainerRepository trainerDao;
    private final TrainingRepository trainingDao;
    private final TrainingMapper trainingMapper;


    @Transactional(readOnly = true)
    public GetTrainingTypesResponse selectAllTrainingTypes(){
        GetTrainingTypesResponse response = new GetTrainingTypesResponse();
        response.setTrainingTypes(trainingDao.getAllTrainingTypes());
        return response;
    }

    @Transactional
    public AddTrainingRequest create(AddTrainingRequest request) {
        Trainee EE = getTrainee(request.getTraineeUsername());
        Trainer ER = getTrainer(request.getTrainerUsername());
        List<Object> eeEr = checkTraineeTrainerConnection(EE, ER);
        Training training = new Training();
        training.setTrainee((Trainee) eeEr.get(0));
        training.setTrainer((Trainer) eeEr.get(1));
        training.setTrainingType(trainingMapper.stringToTrainingType(request.getTrainingType()));
        training.setTrainingName(request.getTrainingName());
        training.setTrainingDate(request.getTrainingDate());
        training.setDuration(request.getTrainingDuration());
        log.info("Training between trainee username: " + request.getTraineeUsername() + " and trainer username: " + request.getTrainerUsername() + " was created");
        return trainingMapper.trainingToAddTrainingRequest(trainingDao.save(training));
    }

    @Transactional
    public GetTrainersResponse updateTrainersList(UpdateTraineeTrainerListRequest request) {
        Trainee trainee = getTrainee(request.getTraineeUsername());
        List<String> exist = trainee.getTrainers().stream()
                .map(trainer -> trainer.getUser().getUsername())
                .collect(Collectors.toList());
        List<String> newTrainers = request.getTrainerUsernames();
        newTrainers.removeAll(exist);
        List<Trainer> trainers = newTrainers.stream()
                .map(userDao::findByUsername)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(trainerDao::findByUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        List<Trainer> ERSlist =  trainee.getTrainers();
        ERSlist.addAll(trainers);
        trainee.setTrainers(ERSlist);
        Trainee updatedTrainee = traineeDao.save(trainee);
        return trainingMapper.traineeToTrainersResponse(updatedTrainee);
    }

    @Transactional(readOnly = true)
    public GetTrainersResponse getTrainersList(String traineeUsername) {
        Trainee trainee = getTrainee(traineeUsername);
        return trainingMapper.traineeToTrainersResponse(trainee);
    }

    @Transactional(readOnly = true)
    public GetTrainersResponse getNotAssignedOnTraineeActiveTrainers(String traineeUsername) {
        Trainee trainee = getTrainee(traineeUsername);
        List<Trainer> existingTrainers = new ArrayList<>(CollectionUtils.emptyIfNull(trainerDao.findAll()));
        List<Trainer> onTrainee = trainee.getTrainers();
        existingTrainers.removeAll(onTrainee);
        GetTrainersResponse response = new GetTrainersResponse();
        response.setTrainers(trainingMapper.trainersToShortTrainersDto(existingTrainers));
        return response;
    }

    @Transactional(readOnly = true)
    public GetTrainingsResponse getTraineeTrainingsList(String username, GetTraineeTrainingsListRequest request) {
        Trainee trainee = getTrainee(username);
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
    public GetTrainingsResponse getTrainerTrainingsList(String username, GetTrainerTrainingsListRequest request) {
        Trainer trainer = getTrainer(username);
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
        List<Trainer> ERSlist = trainee.getTrainers();
        List<Trainee> EESlist = trainer.getTrainees();
        if(!trainee.getTrainers().contains(trainer)) {
            ERSlist.add(trainer);
            trainee.setTrainers(ERSlist);
        } else {
            if(!trainer.getTrainees().contains(trainee)) {
                EESlist.add(trainee);
                trainer.setTrainees(EESlist);
            }
        }
        return List.of(trainee, trainer);
    }

    private Trainee getTrainee(String username) {
        User user = userDao.findByUsername(username).orElseThrow(() -> {
            log.error("No user with username: " + username);
            return new InvalidDataException("userDao.getByUsername(" + username + ")", "No user with username: " + username);
        });
        return traineeDao.findByUser(user).orElseThrow(() -> {
            log.error("No trainee with username " + username);
            return new ProhibitedActionException("No one except trainee could use this method in trainingService, " +
                    "but there are no trainee with username: " + username);
        });
    }

    private Trainer getTrainer(String username) {
        User user = userDao.findByUsername(username).orElseThrow(() -> {
            log.error("No user with username " + username);
            return new InvalidDataException("userDao.getByUsername(" + username + ")", "No user with username: " + username);
        });
        return trainerDao.findByUser(user).orElseThrow(() -> {
            log.error("No trainee with username: " + username);
            return new ProhibitedActionException("No one except trainer could use this method in trainingService, " +
                    "but there are no trainer with username: " + username);
        });
    }

}
