package org.epam.mapper;

import org.apache.commons.collections4.CollectionUtils;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.ShortTraineeDto;
import org.epam.dto.trainerDto.ShortTrainerDto;
import org.epam.dto.trainerDto.TrainerProfileResponse;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface TrainerMapper {
    TrainerMapper INSTANCE = Mappers.getMapper(TrainerMapper.class);

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
    ShortTrainerDto trainerToShortTrainerDto(Trainer trainer);

    List<ShortTrainerDto> trainersToShortTrainersDto(List<Trainer> trainers);

    @Named("traineesToShortTraineesDto")
    default List<ShortTraineeDto> traineesToShortTraineesDto(List<Trainee> trainees) {
        return TraineeMapper.INSTANCE.traineesToShortTraineesDto(
                CollectionUtils.emptyIfNull(trainees).stream().collect(Collectors.toList())
        );
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
