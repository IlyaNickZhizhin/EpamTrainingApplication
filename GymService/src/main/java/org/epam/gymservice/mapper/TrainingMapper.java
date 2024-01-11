package org.epam.gymservice.mapper;

import org.epam.gymservice.dto.reportDto.TrainerWorkloadRequest;
import org.epam.gymservice.dto.reportDto.TrainerWorkloadResponse;
import org.epam.gymservice.dto.trainerDto.TrainerDto;
import org.epam.gymservice.dto.trainingDto.AddTrainingRequest;
import org.epam.gymservice.dto.trainingDto.GetTrainersResponse;
import org.epam.gymservice.dto.trainingDto.TrainingDto;
import org.epam.gymservice.dto.trainingDto.UpdateTraineeTrainerListRequest;
import org.epam.gymservice.model.gymModel.Trainee;
import org.epam.gymservice.model.gymModel.Trainer;
import org.epam.gymservice.model.gymModel.Training;
import org.epam.gymservice.model.gymModel.TrainingType;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {TrainerMapper.class})
public interface TrainingMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "duration", target = "duration")
    @Mapping(source = "trainingType", target = "trainingType", qualifiedByName = "trainingTypeToTrainingName")
    @Mapping(source = "trainer.user.firstName", target = "opponentName")
    @Named("trainingForTrainee")
    TrainingDto traineeTrainingToShortDto(Training training);

    @IterableMapping(qualifiedByName = "trainingForTrainee")
    List<TrainingDto> traineeTrainingsToShortDtos(List<Training> trainings);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "duration", target = "duration")
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

    @Mapping(source = "trainer.user.firstName", target = "firstName")
    @Mapping(source = "trainer.user.lastName", target = "lastName")
    @Mapping(source = "trainer.user.username", target = "username")
    @Mapping(source = "trainer.user.active", target = "active")
    @Mapping(source = "duration", target = "duration")
    @Mapping(source = "trainingDate", target = "trainingDate")
    TrainerWorkloadRequest trainingToWorkloadRequest(Training training);

    @Mapping(target = "trainingSessions", ignore = true)
    TrainerWorkloadResponse trainerWorkloadRequestToResponse(TrainerWorkloadRequest request);

    @Named("tNameToTrainingType")
    default TrainingType stringToTrainingType(TrainingType.TrainingName type) {
        return TrainingType.of(type);
    }

    @Named("trainersToSortTrainersDto")
    default List<TrainerDto> trainingsToShortTrainersDto(List<Trainer> trainers) {
        TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);
        return trainerMapper.trainersToTrainersDto(trainers);
    }

    @Named("trainersToTrainersDto")
    default List<TrainerDto> trainersToShortTrainersDto(List<Trainer> trainers) {
        TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);
        return trainerMapper.trainersToTrainersDto(trainers);
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
