package org.epam.mapper;

import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeDto;
import org.epam.dto.trainerDto.TrainerDto;
import org.epam.dto.trainerDto.TrainerProfileResponse;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TraineeMapper.class})
public interface TrainerMapper {

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.password", target = "password")
    RegistrationResponse trainerToRegistrationResponse(Trainer trainer);

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.active", target = "active")
    @Mapping(source = "specialization", target = "specialization", qualifiedByName = "trainingTypeToTrainingName")
    @Mapping(source = "trainees", target = "trainees", qualifiedByName = "traineesToShortTraineesDto")
    TrainerProfileResponse trainerToProfileResponse(Trainer trainer);

    @Mapping(source = "user.firstName", target = "firstname")
    @Mapping(source = "user.lastName", target = "lastname")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "specialization", target = "specialization", qualifiedByName = "trainingTypeToTrainingName")
    TrainerDto trainerToShortTrainerDto(Trainer trainer);

    List<TrainerDto> trainersToShortTrainersDto(List<Trainer> trainers);

    @Named("traineesToShortTraineesDto")
    default List<TraineeDto> traineesToShortTraineesDto(List<Trainee> trainees) {
        TraineeMapper traineeMapper = Mappers.getMapper(TraineeMapper.class);
        return traineeMapper.traineesToShortTraineesDto(
                trainees);
    }

    @Named("tNameToTrainingType")
    default TrainingType stringToTrainingType(TrainingType.TrainingName type) {
        return TrainingType.of(type);
    }

    @Named("trainingTypeToTrainingName")
    default TrainingType.TrainingName trainingTypeToString(TrainingType trainingType) {
        return trainingType.getTrainingName();
    }
}
