package org.epam.mapper;

import org.epam.dto.trainerDto.TrainerDto;
import org.epam.dto.trainingDto.AddTrainingRequest;
import org.epam.dto.trainingDto.GetTrainersResponse;
import org.epam.dto.trainingDto.TrainingDto;
import org.epam.dto.trainingDto.UpdateTraineeTrainerListRequest;
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
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {TrainerMapper.class})
public interface TrainingMapper {

    @Mapping(source = "trainingType", target = "trainingType", qualifiedByName = "trainingTypeToTrainingName")
    @Mapping(source = "trainer.user.firstName", target = "opponentName")
    @Named("trainingForTrainee")
    TrainingDto traineeTrainingToShortDto(Training training);

    @IterableMapping(qualifiedByName = "trainingForTrainee")
    List<TrainingDto> traineeTrainingsToShortDtos(List<Training> trainings);

    @Mapping(source = "trainingType", target = "trainingType", qualifiedByName = "trainingTypeToTrainingName")
    @Mapping(source = "trainee.user.firstName", target = "opponentName")
    @Named("trainingForTrainer")
    TrainingDto trainerTrainingToShortDto(Training training);

    @IterableMapping(qualifiedByName = "trainingForTrainer")
    List<TrainingDto> trainerTrainingsToShortDtos(List<Training> trainings);

    @Mapping(target = "trainers", source = "trainers", qualifiedByName = "trainersToSortTrainersDto")
    GetTrainersResponse traineeToTrainersResponse(Trainee trainee);

    @Mapping(target = "traineeUsername", source = "trainee.user.username")
    @Mapping(target = "trainerUsername", source = "trainer.user.username")
    @Mapping(target = "trainingType", source = "trainingType.trainingName")
    @Mapping(target = "trainingDuration", source = "duration")
    AddTrainingRequest trainingToAddTrainingRequest(Training training);

    @Named("tNameToTrainingType")
    default TrainingType stringToTrainingType(TrainingType.TrainingName type) {
        return TrainingType.of(type);
    }

    @Named("trainersToSortTrainersDto")
    default List<TrainerDto> trainingsToShortTrainersDto(List<Trainer> trainers) {
        TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);
        return trainerMapper.trainersToShortTrainersDto(trainers);
    }

    @Named("trainersToShortTrainersDto")
    default List<TrainerDto> trainersToShortTrainersDto(List<Trainer> trainers) {
        TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);
        return trainerMapper.trainersToShortTrainersDto(trainers);
    }

    @Mapping(target = "traineeUsername", source = "trainee.user.username")
    @Mapping(target = "trainerUsernames", source = "trainee.trainers", qualifiedByName = "trainersToUsernames")
    default UpdateTraineeTrainerListRequest traineeToUpdateTrainerListRequest(Trainee trainee) {
        UpdateTraineeTrainerListRequest request = new UpdateTraineeTrainerListRequest();
        request.setTraineeUsername(trainee.getUser().getUsername());
        request.setTrainerUsernames(trainee.getTrainers().stream()
                .map(trainer -> trainer.getUser().getUsername())
                .collect(Collectors.toList()));
        return request;
    }
}
