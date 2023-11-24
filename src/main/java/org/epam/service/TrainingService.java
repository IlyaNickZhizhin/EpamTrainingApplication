package org.epam.service;

import lombok.extern.slf4j.Slf4j;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.TrainingDaoImpl;
import org.epam.dto.TraineeDto;
import org.epam.dto.TrainerDto;
import org.epam.dto.TrainingDto;
import org.epam.exceptions.VerificationException;
import org.epam.mapper.GymMapper;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
    PasswordChecker passwordChecker;

    @Autowired
    GymMapper gymMapper;

    @Transactional
    public TrainingDto create(TrainerDto trainerDto,
                           TraineeDto traineeDto, TrainingDto trainingDto) {
        try {
            trainerService.selectByUsername(trainerDto);
        } catch (VerificationException e) {
            try {
                traineeService.selectByUsername(traineeDto);
            } catch (VerificationException ex) {
                throw new VerificationException("Incorrect password for training creating");
            }
        }
        Training training = gymMapper.trainingDtoToTraining(trainingDto);
        training.setTrainer(gymMapper.trainerDtoToTrainer(trainerDto));
        training.setTrainee(gymMapper.traineeDtoToTrainee(traineeDto));
        return gymMapper.trainingToTrainingDto(
                trainingDao.create(training)
        );
    }

    @Transactional(readOnly = true)
    public List<TrainerDto> updateTrainersList(TraineeDto traineeForUpdateList) {
        traineeService.selectByUsername(traineeForUpdateList); // password cheking
        List<Trainer> trainers = trainingDao.updateTrainersList(
                gymMapper.traineeDtoToTrainee(traineeForUpdateList));
        return gymMapper.trainersToTrainerDtos(trainers);
    }

    @Transactional(readOnly = true)
    public List<TrainerDto> getAllTrainersAvalibleForTrainee(TraineeDto traineeDto) {
        traineeService.selectByUsername(traineeDto); // password cheking
        List<Trainer> existingTrainers = trainerService.selectAll();
        List<Trainer> avalibleTrainers = trainingDao.getAllTrainersAvalibleForTrainee(
                gymMapper.traineeDtoToTrainee(traineeDto), existingTrainers);
        return gymMapper.trainersToTrainerDtos(avalibleTrainers);
    }

    @Transactional(readOnly = true)
    public List<TrainingDto> getTrainingsByUsernameAndTrainingTypes (TraineeDto traineeDto,
                                                                          List<TrainingType> types) {
        TraineeDto trainee = traineeService.selectByUsername(traineeDto);
        List<Training> trainings = trainingDao
                .getAllByUsernameAndTrainingTypes
                        (types, gymMapper.traineeDtoToTrainee(traineeDto));
        return gymMapper.trainingsToTrainingDtos(trainings);
    }

}
