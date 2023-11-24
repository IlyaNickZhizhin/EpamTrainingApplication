package org.epam.mapper;

import org.epam.dto.TraineeDto;
import org.epam.dto.TrainerDto;
import org.epam.dto.TrainingDto;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface GymMapper {

    GymMapper INSTANCE = Mappers.getMapper(GymMapper.class);

    @Mapping(source = "user.firstName", target = "firstname")
    @Mapping(source = "user.lastName", target = "lastname")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.password", target = "password")
    @Mapping(source = "user.active", target = "active")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "address", target = "address")
    TraineeDto traineeToTraineeDto(Trainee trainee);

    @Mapping(source = "user.firstName", target = "firstname")
    @Mapping(source = "user.lastName", target = "lastname")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.password", target = "password")
    @Mapping(source = "user.active", target = "active")
    @Mapping(source = "specialization", target = "specialization", qualifiedByName = "trainingTypeToTrainingName")
    TrainerDto trainerToTrainerDto(Trainer trainer);

    @Mapping(source = "trainingName", target = "trainingName")
    @Mapping(source = "trainingType", target = "trainingType", qualifiedByName = "trainingTypeToTrainingName")
    @Mapping(source = "trainingDate", target = "trainingDate")
    @Mapping(source = "duration", target = "duration")
    @Mapping(source = "trainer", target = "trainer")
    @Mapping(source = "trainee", target = "trainee")
    TrainingDto trainingToTrainingDto(Training training);

    @Mapping(source = "firstname", target = "user.firstName")
    @Mapping(source = "lastname", target = "user.lastName")
    @Mapping(source = "username", target = "user.username")
    @Mapping(source = "password", target = "user.password")
    @Mapping(source = "active", target = "user.active")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "address", target = "address")
    Trainee traineeDtoToTrainee(TraineeDto traineeDto);

    @Mapping(source = "firstname", target = "user.firstName")
    @Mapping(source = "lastname", target = "user.lastName")
    @Mapping(source = "username", target = "user.username")
    @Mapping(source = "password", target = "user.password")
    @Mapping(source = "active", target = "user.active")
    @Mapping(source = "specialization", target = "specialization", qualifiedByName = "tNameToTrainingType")
    Trainer trainerDtoToTrainer(TrainerDto trainerDto);

    @Mapping(source = "trainingName", target = "trainingName")
    @Mapping(source = "trainingType", target = "trainingType")
    @Mapping(source = "trainingDate", target = "trainingDate")
    @Mapping(source = "duration", target = "duration")
    @Mapping(source = "trainer", target = "trainer")
    @Mapping(source = "trainee", target = "trainee")
    Training trainingDtoToTraining(TrainingDto trainingDto);

    List<Trainer> trainerDtosToTrainers(List<TrainerDto> trainerDtos);

    List<TrainerDto> trainersToTrainerDtos(List<Trainer> trainers);

    List<Training> trainingDtosToTrainings(List<TrainingDto> trainingDtos);

    List<TrainingDto> trainingsToTrainingDtos(List<Training> trainings);

    @Named("tNameToTrainingType")
    default TrainingType stringToTrainingType(TrainingType.TrainingName type) {
        return TrainingType.of(type);
    }

    @Named("trainingTypeToTrainingName")
    default TrainingType.TrainingName trainingTypeToString(TrainingType trainingType) {
        return trainingType.getTrainingName();
    }
}
