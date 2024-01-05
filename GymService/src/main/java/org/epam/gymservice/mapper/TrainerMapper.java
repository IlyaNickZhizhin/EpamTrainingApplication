package org.epam.gymservice.mapper;

import org.epam.gymservice.dto.ActivateDeactivateRequest;
import org.epam.gymservice.dto.ChangeLoginRequest;
import org.epam.gymservice.dto.RegistrationResponse;
import org.epam.gymservice.dto.traineeDto.TraineeDto;
import org.epam.gymservice.dto.trainerDto.TrainerDto;
import org.epam.gymservice.dto.trainerDto.UpdateTrainerProfileRequest;
import org.epam.gymservice.dto.trainerDto.TrainerProfileResponse;
import org.epam.gymservice.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.gymservice.model.gymModel.Trainee;
import org.epam.gymservice.model.gymModel.Trainer;
import org.epam.gymservice.model.gymModel.TrainingType;
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

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "specialization", target = "specialization", qualifiedByName = "trainingTypeToTrainingName")
    TrainerDto trainerToTrainerDto(Trainer trainer);

    List<TrainerDto> trainersToTrainersDto(List<Trainer> trainers);

    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "specialization", source = "specialization", qualifiedByName = "trainingTypeToTrainingName")
    TrainerRegistrationRequest trainerToRegistrationRequest(Trainer trainer);

    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "active", source = "user.active")
    @Mapping(target = "specialization", source = "specialization", qualifiedByName = "trainingTypeToTrainingName")
    UpdateTrainerProfileRequest trainerToUpdateRequest(Trainer trainer);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "oldPassword", ignore = true)
    @Mapping(target = "newPassword", ignore = true)
    ChangeLoginRequest trainerDtoToChangeLoginRequest(TrainerDto trainer);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "oldPassword", source = "user.password")
    @Mapping(target = "newPassword", ignore = true)
    ChangeLoginRequest trainerToChangeLoginRequest(Trainer trainer);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "active", source = "user.active")
    ActivateDeactivateRequest trainerToActivateDeactivateRequest(Trainer trainer);

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

    @Named("trainerToUsername")
    default String trainerToUsername(Trainer trainer){
        return trainer.getUser().getUsername();
    }
}
