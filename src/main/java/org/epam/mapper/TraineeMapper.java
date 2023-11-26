package org.epam.mapper;

import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeDto;
import org.epam.dto.traineeDto.TraineeProfileResponse;
import org.epam.dto.traineeDto.UpdateTraineeProfileRequest;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TraineeMapper {
    RegistrationResponse traineeToRegistrationResponse(TraineeDto traineeDto);
    TraineeDto updateTraineeProfileResponseToTraineeDto(UpdateTraineeProfileRequest request);
    TraineeProfileResponse traineeDtoToProfileResponse(TraineeDto traineeDto);
}
