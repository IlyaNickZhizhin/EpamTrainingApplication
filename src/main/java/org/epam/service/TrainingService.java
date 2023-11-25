package org.epam.service;

import lombok.extern.slf4j.Slf4j;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.TrainingDaoImpl;
import org.epam.dto.traineeDto.TraineeDto;
import org.epam.dto.trainerDto.TrainerDto;
import org.epam.dto.trainingDto.TrainingDto;
import org.epam.exceptions.VerificationException;
import org.epam.mapper.GymGeneralMapper;
import org.epam.mapper.TrainingMapper;
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
    TrainingMapper trainingMapper;

    @Autowired
    GymGeneralMapper gymGeneralMapper;

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
        Training training = gymGeneralMapper.trainingDtoToTraining(trainingDto);
        training.setTrainer(gymGeneralMapper.trainerDtoToTrainer(trainerDto));
        training.setTrainee(gymGeneralMapper.traineeDtoToTrainee(traineeDto));
        return gymGeneralMapper.trainingToTrainingDto(
                trainingDao.create(training)
        );
    }

    @Transactional(readOnly = true)
    public List<TrainerDto> updateTrainersList(TraineeDto traineeForUpdateList) {
        traineeService.selectByUsername(traineeForUpdateList); // password cheking
        List<Trainer> trainers = trainingDao.updateTrainersList(
                gymGeneralMapper.traineeDtoToTrainee(traineeForUpdateList));
        return gymGeneralMapper.trainersToTrainerDtos(trainers);
    }

    @Transactional(readOnly = true)
    public List<TrainerDto> getAllTrainersAvalibleForTrainee(TraineeDto traineeDto) {
        traineeService.selectByUsername(traineeDto); // password cheking
        List<Trainer> existingTrainers = trainerService.selectAll();
        List<Trainer> avalibleTrainers = trainingDao.getAllTrainersAvalibleForTrainee(
                gymGeneralMapper.traineeDtoToTrainee(traineeDto), existingTrainers);
        return gymGeneralMapper.trainersToTrainerDtos(avalibleTrainers);
    }

    @Transactional(readOnly = true)
    public List<TrainingDto> getTrainingsByUsernameAndTrainingTypes (TraineeDto traineeDto,
                                                                          List<TrainingType> types) {
        TraineeDto trainee = traineeService.selectByUsername(traineeDto);
        List<Training> trainings = trainingDao
                .getAllByUsernameAndTrainingTypes
                        (types, gymGeneralMapper.traineeDtoToTrainee(traineeDto));
        return gymGeneralMapper.trainingsToTrainingDtos(trainings);
    }

}
