package org.epam.mapper;

import org.epam.dto.trainerDto.ShortTrainerDto;
import org.epam.dto.trainingDto.AddTrainingRequest;
import org.epam.dto.trainingDto.GetTrainersResponse;
import org.epam.dto.trainingDto.ShortTrainingDto;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TrainingMapper {

    TrainingMapper INSTANCE = Mappers.getMapper(TrainingMapper.class);
    @Mapping(source = "trainingName", target = "trainingName")
    @Mapping(source = "trainingType", target = "trainingType", qualifiedByName = "trainingTypeToTrainingName")
    @Mapping(source = "trainingDate", target = "trainingDate")
    @Mapping(source = "duration", target = "duration")
    @Mapping(source = "trainer.user.firstName", target = "opponentName")
    @Named("trainingForTrainee")
    ShortTrainingDto traineeTrainingToShortDto(Training training);

    @Named("trainingsForTrainee")
    @IterableMapping(qualifiedByName = "trainingForTrainee")
    List<ShortTrainingDto> traineeTrainingsToShortDtos(List<Training> trainings);

    @Mapping(source = "trainingName", target = "trainingName")
    @Mapping(source = "trainingType", target = "trainingType", qualifiedByName = "trainingTypeToTrainingName")
    @Mapping(source = "trainingDate", target = "trainingDate")
    @Mapping(source = "duration", target = "duration")
    @Mapping(source = "trainee.user.firstName", target = "opponentName")
    @Named("trainingForTrainer")
    ShortTrainingDto trainerTrainingToShortDto(Training training);

    @Named("trainingsForTrainer")
    @IterableMapping(qualifiedByName = "trainingForTrainer")
    List<ShortTrainingDto> trainerTrainingsToShortDtos(List<Training> trainings);

    @Mapping(target = "trainers", source = "trainers", qualifiedByName = "trainersToSortTrainersDto")
    GetTrainersResponse traineeToTrainersResponse(Trainee trainee);

    @Mapping(target = "traineeUsername", source = "trainee.user.username")
    @Mapping(target = "trainerUsername", source = "trainer.user.username")
    @Mapping(target = "trainingType", source = "trainingType.trainingName")
    @Mapping(target = "trainingName", source = "trainingName")
    @Mapping(target = "trainingDate", source = "trainingDate")
    @Mapping(target = "trainingDuration", source = "duration")
    AddTrainingRequest trainingToAddTrainingRequest(Training training);

    @Named("tNameToTrainingType")
    default TrainingType stringToTrainingType(TrainingType.TrainingName type) {
        return TrainingType.of(type);
    }

    @Named("trainingTypeToTrainingName")
    default TrainingType.TrainingName trainingTypeToString(TrainingType trainingType) {
        return trainingType.getTrainingName();
    }
    @Named("trainersToSortTrainersDto")
    default List<ShortTrainerDto> trainingsToShortTrainersDto(List<Trainer> trainers) {
        return TrainerMapper.INSTANCE.trainersToShortTrainersDto(trainers);
    }

    @Named("trainersToShortTrainersDto")
    default List<ShortTrainerDto> trainersToShortTrainersDto(List<Trainer> trainers) {
        return TrainerMapper.INSTANCE.trainersToShortTrainersDto(trainers);
    }
}
