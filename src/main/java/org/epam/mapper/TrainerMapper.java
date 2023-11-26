package org.epam.mapper;

import org.epam.dto.RegistrationResponse;
import org.epam.dto.trainerDto.TrainerDto;
import org.epam.dto.trainerDto.TrainerProfileResponse;
import org.epam.dto.trainerDto.UpdateTrainerProfileRequest;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TrainerMapper {
    RegistrationResponse trainerToRegistrationResponse(TrainerDto trainerDto);
    TrainerDto updateTrainerProfileRequestToTrainerDto(UpdateTrainerProfileRequest request);
    TrainerProfileResponse trainerDtoToUpdateResponse(TrainerDto trainerDto);
}
