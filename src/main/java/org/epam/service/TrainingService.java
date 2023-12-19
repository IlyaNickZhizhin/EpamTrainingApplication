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
    private final UserRepository userRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
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
        Trainee trainee = getTrainee(request.getTraineeUsername());
        Trainer trainer = getTrainer(request.getTrainerUsername());
        List<Object> traineeTrainerConnection = checkTraineeTrainerConnection(trainee, trainer);
        Training training = new Training();
        training.setTrainee((Trainee) traineeTrainerConnection.get(0));
        training.setTrainer((Trainer) traineeTrainerConnection.get(1));
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
                .map(userRepository::findByUsername)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(trainerRepository::findByUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        List<Trainer> trainerList =  trainee.getTrainers();
        trainerList.addAll(trainers);
        trainee.setTrainers(trainerList);
        Trainee updatedTrainee = traineeRepository.save(trainee);
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
        List<Trainer> existingTrainers = new ArrayList<>(CollectionUtils.emptyIfNull(trainerRepository.findAll()));
        List<Trainer> onTrainee = trainee.getTrainers();
        existingTrainers.removeAll(onTrainee);
        GetTrainersResponse response = new GetTrainersResponse();
        response.setTrainers(trainingMapper.trainersToShortTrainersDto(existingTrainers));
        return response;
    }

    private List<Object> checkTraineeTrainerConnection(Trainee trainee, Trainer trainer){
        List<Trainer> trainerList = trainee.getTrainers();
        List<Trainee> traineeList = trainer.getTrainees();
        if(!trainee.getTrainers().contains(trainer)) {
            trainerList.add(trainer);
            trainee.setTrainers(trainerList);
        } else {
            if(!trainer.getTrainees().contains(trainee)) {
                traineeList.add(trainee);
                trainer.setTrainees(traineeList);
            }
        }
        return List.of(trainee, trainer);
    }

    private Trainer getTrainer(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("No user with username " + username);
            return new InvalidDataException("userDao.getByUsername(" + username + ")", "No user with username: " + username);
        });
        return trainerRepository.findByUser(user).orElseThrow(() -> {
            log.error("No trainee with username: " + username);
            return new ProhibitedActionException("No one except trainer could use this method in trainingService, " +
                    "but there are no trainer with username: " + username);
        });
    }
    private Trainee getTrainee(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("No user with username: " + username);
            return new InvalidDataException("userDao.getByUsername(" + username + ")", "No user with username: " + username);
        });
        return traineeRepository.findByUser(user).orElseThrow(() -> {
            log.error("No trainee with username " + username);
            return new ProhibitedActionException("No one except trainee could use this method in trainingService, " +
                    "but there are no trainee with username: " + username);
        });
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

}
