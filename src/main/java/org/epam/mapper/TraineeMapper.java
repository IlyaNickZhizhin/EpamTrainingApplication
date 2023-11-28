package org.epam.mapper;

import org.apache.commons.collections4.CollectionUtils;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.ShortTraineeDto;
import org.epam.dto.traineeDto.TraineeProfileResponse;
import org.epam.dto.traineeDto.UpdateTraineeProfileRequest;
import org.epam.dto.trainerDto.ShortTrainerDto;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface TraineeMapper {
    TraineeMapper INSTANCE = Mappers.getMapper(TraineeMapper.class);

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.password", target = "password")
    RegistrationResponse traineeToRegistrationResponse(Trainee trainee);

    @Mapping(source = "user.firstName", target = "firstname")
    @Mapping(source = "user.lastName", target = "lastname")
    @Mapping(source = "user.active", target = "active")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "trainers", target = "trainers", qualifiedByName = "trainersToTrainersDto")
    TraineeProfileResponse traineeToProfileResponse(Trainee trainee);

    @Mapping(source = "user.firstName", target = "firstname")
    @Mapping(source = "user.lastName", target = "lastname")
    @Mapping(source = "user.username", target = "username")
    ShortTraineeDto traineeToShortTraineeDto(Trainee trainee);

    List<ShortTraineeDto> traineesToShortTraineesDto(List<Trainee> trainees);

    @Named("trainersToTrainersDto")
    default List<ShortTrainerDto> trainersToTrainersDto(List<Trainer> trainers) {
        return TrainerMapper.INSTANCE.trainersToShortTrainersDto(
                CollectionUtils.emptyIfNull(trainers).stream().collect(Collectors.toList()));
    }
}
