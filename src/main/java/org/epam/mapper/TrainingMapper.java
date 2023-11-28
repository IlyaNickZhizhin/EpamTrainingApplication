package org.epam.mapper;

import org.epam.dto.trainerDto.ShortTrainerDto;
import org.epam.dto.trainingDto.GetTrainersResponse;
import org.epam.dto.trainingDto.GetTrainingsResponse;
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
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
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

    @Mapping(target = "trainers", source = "trainings", qualifiedByName = "trainingsToTrainers")
    GetTrainersResponse traineeToTrainersResponse(Trainee trainee);


    @Named("tNameToTrainingType")
    default TrainingType stringToTrainingType(TrainingType.TrainingName type) {
        return TrainingType.of(type);
    }

    @Named("trainingTypeToTrainingName")
    default TrainingType.TrainingName trainingTypeToString(TrainingType trainingType) {
        return trainingType.getTrainingName();
    }
    @Named("trainingsToTrainers")
    default List<ShortTrainerDto> trainingsToShortTrainersDto(List<Training> trainings) {
        return TraineeMapper.INSTANCE.trainingsToShortTrainersDto(trainings);
    }

    @Named("trainersToShortTrainersDto")
    default List<ShortTrainerDto> trainersToShortTrainersDto(List<Trainer> trainers) {
        return TrainerMapper.INSTANCE.trainersToShortTrainersDto(trainers);
    }
}
