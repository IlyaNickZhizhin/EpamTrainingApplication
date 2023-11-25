package org.epam.mapper;

import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.ShotTraineeDto;
import org.epam.dto.traineeDto.TraineeDto;
import org.epam.dto.traineeDto.UpdateTraineeProfileRequest;
import org.epam.dto.traineeDto.UpdateTraineeProfileResponse;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TraineeMapper {
    RegistrationResponse traineeToRegistrationResponse(TraineeDto traineeDto);
    TraineeDto updateTraineeProfileResponseToTraineeDto(UpdateTraineeProfileRequest request);
    UpdateTraineeProfileResponse traineeDtoToUpdateResponse(TraineeDto traineeDto);
}
