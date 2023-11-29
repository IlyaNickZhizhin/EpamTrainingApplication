package org.epam;

import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.LoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.dto.traineeDto.UpdateTraineeProfileRequest;
import org.epam.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.dto.trainerDto.UpdateTrainerProfileRequest;
import org.epam.dto.trainingDto.AddTrainingRequest;
import org.epam.dto.trainingDto.UpdateTraineeTrainerListRequest;
import org.epam.model.User;
import org.epam.model.gymModel.Role;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.stream.Collectors;

@Mapper
public interface TestMapper {

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
    @Mapping(target = "newPassword", ignore = true)
    ChangeLoginRequest roleToChangeLoginRequest(Role role);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "active", source = "user.active")
    ActivateDeactivateRequest roleToActivateDeactivateRequest(Role role);

    @Mapping(target = "traineeUsername", source = "trainee.user.username")
    @Mapping(target = "trainerUsername", source = "trainer.user.username")
    @Mapping(target = "trainingType", source = "trainingType.trainingName")
    @Mapping(target = "trainingName", source = "trainingName")
    @Mapping(target = "trainingDate", source = "trainingDate")
    @Mapping(target = "trainingDuration", source = "duration")
    AddTrainingRequest trainingToAddTrainingRequest(Training training);

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
    
    @Named("trainerToUsername")
    default String trainerToUsername(Trainer trainer){
        return trainer.getUser().getUsername();
    }

}
