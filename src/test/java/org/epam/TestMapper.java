package org.epam;

import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.LoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.dto.traineeDto.UpdateTraineeProfileRequest;
import org.epam.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.dto.trainerDto.UpdateTrainerProfileRequest;
import org.epam.model.User;
import org.epam.model.gymModel.Role;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TestMapper {

    TestMapper INSTANCE = Mappers.getMapper(TestMapper.class);

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

    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "specialization", source = "specialization", qualifiedByName = "trainingTypeToString")
    TrainerRegistrationRequest trainerToRegistrationRequest(Trainer trainer);

    @Mapping(target = "firstname", source = "user.firstName")
    @Mapping(target = "lastname", source = "user.lastName")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "active", source = "user.active")
    @Mapping(target = "specialization", source = "specialization", qualifiedByName = "trainingTypeToString")
    UpdateTrainerProfileRequest trainerToUpdateRequest(Trainer trainer);

    @Named("trainingTypeToString")
    default TrainingType.TrainingName trainingTypeToString(TrainingType trainingType) {
        return trainingType.getTrainingName();
    }

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "oldPassword", source = "user.password")
    ChangeLoginRequest roleToChangeLoginRequest(Role role);

}
