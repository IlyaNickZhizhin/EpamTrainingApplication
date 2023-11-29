package org.epam.mapper;

import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.LoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeDto;
import org.epam.dto.traineeDto.TraineeProfileResponse;
import org.epam.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.dto.traineeDto.UpdateTraineeProfileRequest;
import org.epam.dto.trainerDto.TrainerDto;
import org.epam.model.User;
import org.epam.model.gymModel.Role;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TrainerMapper.class})
public interface TraineeMapper {

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
        TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);
        return trainerMapper.trainersToShortTrainersDto(
                trainers);
    }

    LoginRequest userToLoginRequest(User user);

    RegistrationResponse userToRegistrationResponce(User user);
    @Mapping(target = "firstname", source = "user.firstName")
    @Mapping(target = "lastname", source = "user.lastName")
    TraineeRegistrationRequest traineeToRegistrationRequest(Trainee trainee);

    @Mapping(target = "firstname", source = "user.firstName")
    @Mapping(target = "lastname", source = "user.lastName")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "active", source = "user.active")
    UpdateTraineeProfileRequest traineeToUpdateRequest(Trainee trainee);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "oldPassword", source = "user.password")
    @Mapping(target = "newPassword", ignore = true)
    ChangeLoginRequest roleToChangeLoginRequest(Role role);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "active", source = "user.active")
    ActivateDeactivateRequest roleToActivateDeactivateRequest(Role role);
}
