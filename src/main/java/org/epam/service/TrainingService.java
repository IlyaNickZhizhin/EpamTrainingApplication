package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.epam.dao.TraineeRepository;
import org.epam.dao.TrainerRepository;
import org.epam.dao.TrainingRepository;
import org.epam.dao.UserRepository;
import org.epam.dto.trainingDto.AddTrainingRequest;
import org.epam.dto.trainingDto.GetTrainersResponse;
import org.epam.dto.trainingDto.GetTrainingTypesResponse;
import org.epam.dto.trainingDto.UpdateTraineeTrainerListRequest;
import org.epam.exceptions.InvalidDataException;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.mapper.TrainingMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
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
        log.info("Training between trainee #: " + trainee.getId() + " and trainer #: " + trainer.getId() + " was created");
        return trainingMapper.trainingToAddTrainingRequest(trainingDao.save(training));
    }

    @Transactional
    public GetTrainersResponse updateTrainersList(UpdateTraineeTrainerListRequest request) {
        Trainee trainee = getTrainee(request.getTraineeUsername());
        List<String> exist = trainee.getTrainers().stream()
                .map(trainer -> trainer.getUser().getUsername())
                .toList();
        List<String> newTrainers = request.getTrainerUsernames();
        newTrainers.removeAll(exist);
        List<User> usersOfNewTrainersToAdd = newTrainers.stream()
                .map(userRepository::findByUsername)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        List<Trainer> trainersToAdd = usersOfNewTrainersToAdd.stream()
                .map(trainerRepository::findByUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        List<Trainer> trainerList =  trainee.getTrainers();
        trainerList.addAll(trainersToAdd);
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
        log.info("Start finding trainers for trainee #: " + trainee.getId());
        List<Trainer> existingTrainers = new ArrayList<>(CollectionUtils.emptyIfNull(trainerRepository.findAll()));
        log.info("Existing trainers size: " + existingTrainers.size());
        List<Trainer> onTrainee = trainee.getTrainers();
        log.info("Trainee already has " + onTrainee.size() + "trainers");
        existingTrainers.removeAll(onTrainee);
        log.info("Possible trainers for trainee #: " +trainee.getId() + "size" + existingTrainers.size());
        GetTrainersResponse response = new GetTrainersResponse();
        response.setTrainers(trainingMapper.trainersToShortTrainersDto(existingTrainers));
        return response;
    }

    private List<Object> checkTraineeTrainerConnection(Trainee trainee, Trainer trainer){
        List<Trainer> trainerList = trainee.getTrainers();
        List<Trainee> traineeList = trainer.getTrainees();
        log.info("Trainee #: " + trainee.getId() + " has " + trainerList.size() + " trainers");
        if(!trainee.getTrainers().contains(trainer)) {
            trainerList.add(trainer);
            trainee.setTrainers(trainerList);
            log.info("Trainer #: " + trainer.getId() + " added to trainee #: " + trainee.getId());
        } else {
            log.info("Trainer #: " + trainer.getId() + " already assigned to trainee #: " + trainee.getId());
            if(!trainer.getTrainees().contains(trainee)) {
                traineeList.add(trainee);
                trainer.setTrainees(traineeList);
                log.info("Trainee #: " + trainee.getId() + " added to trainer #: " + trainer.getId());
            }
        }
        log.info("Trainee #: " + trainee.getId() + " has " + trainerList.size() + " trainers");
        return List.of(trainee, trainer);
    }

    private Trainer getTrainer(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("No user with username " + username);
            return new InvalidDataException("userDao.getByUsername(" + username + ")", "No user with username: " + username);
        });
        return trainerRepository.findByUser(user).orElseThrow(() -> {
            log.error("No trainee with user: " + user.getId());
            return new ProhibitedActionException("No one except trainer could use this method in trainingService, " +
                    "but there are no trainer with user: " + user.getId());
        });
    }
    private Trainee getTrainee(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("No user with username: " + username);
            return new InvalidDataException("userDao.getByUsername(" + username + ")", "No user with username: " + username);
        });
        log.info("User with id: " + user.getId() + " found");
        return traineeRepository.findByUser(user).orElseThrow(() -> {
            log.error("No trainee with user #" + user.getId());
            return new ProhibitedActionException("No one except trainee could use this method in trainingService, " +
                    "but there are no trainee with user #: " + user.getId());
        });
    }

    private List<Training> trainingFilterByDate(List<Training> trainings, LocalDate periodFrom, LocalDate periodTo) {
        if (periodFrom != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainingDate().isAfter(periodFrom))
                    .collect(Collectors.toList());
            log.info("Trainings filtered by periodFrom: " + periodFrom + "to size: " + trainings.size());
        }
        if (periodTo != null) {
            trainings = trainings.stream()
                    .filter(training -> training.getTrainingDate().isBefore(periodTo))
                    .collect(Collectors.toList());
            log.info("Trainings filtered by periodTo: " + periodTo + "to size: " + trainings.size());
        }
        return trainings;
    }

}
