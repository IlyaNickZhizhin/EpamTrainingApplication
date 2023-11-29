package org.epam.mapper;

import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeDto;
import org.epam.dto.traineeDto.TraineeProfileResponse;
import org.epam.dto.trainerDto.TrainerDto;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TraineeMapper {
    TraineeMapper INSTANCE = Mappers.getMapper(TraineeMapper.class);

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.password", target = "password")
    RegistrationResponse traineeToRegistrationResponse(Trainee trainee);

    @Mapping(source = "user.firstName", target = "firstname")
    @Mapping(source = "user.lastName", target = "lastname")
    @Mapping(source = "user.active", target = "active")
    @Mapping(source = "trainers", target = "trainers", qualifiedByName = "trainersToTrainersDto")
    TraineeProfileResponse traineeToProfileResponse(Trainee trainee);

    @Mapping(source = "user.firstName", target = "firstname")
    @Mapping(source = "user.lastName", target = "lastname")
    @Mapping(source = "user.username", target = "username")
    TraineeDto traineeToShortTraineeDto(Trainee trainee);

    List<TraineeDto> traineesToShortTraineesDto(List<Trainee> trainees);

    @Named("trainersToTrainersDto")
    default List<TrainerDto> trainersToTrainersDto(List<Trainer> trainers) {
        return TrainerMapper.INSTANCE.trainersToShortTrainersDto(
                trainers);
    }
}
