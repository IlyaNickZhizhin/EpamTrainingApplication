package org.epam.mapper;

import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeProfileResponse;
import org.epam.dto.traineeDto.UpdateTraineeProfileRequest;
import org.epam.dto.trainerDto.ShortTrainerDto;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
@Component
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
    @Mapping(source = "trainings", target = "trainers", qualifiedByName = "trainingsToTrainers")
    TraineeProfileResponse traineeToProfileResponse(Trainee trainee);

    @Named("trainingsToTrainers")
    default List<ShortTrainerDto> trainingsToShortTrainersDto(List<Training> trainings) {
        return trainings.stream().map(training -> {
            ShortTrainerDto shortTrainerDto = new ShortTrainerDto();
            shortTrainerDto.setFirstname(training.getTrainer().getUser().getFirstName());
            shortTrainerDto.setLastname(training.getTrainer().getUser().getLastName());
            shortTrainerDto.setUsername(training.getTrainer().getUser().getUsername());
            shortTrainerDto.setSpecialization(training.getTrainer().getSpecialization().getTrainingName());
            return shortTrainerDto;
        }).collect(Collectors.toList());
    }
}
