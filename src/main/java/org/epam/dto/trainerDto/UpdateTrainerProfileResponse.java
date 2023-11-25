package org.epam.dto.trainerDto;

import org.epam.dto.traineeDto.TraineeDto;
import org.epam.model.gymModel.TrainingType;

import java.util.List;

public class UpdateTrainerProfileResponse {
    private String username;
    private String firstName;
    private String lastName;
    private TrainingType.TrainingName specialization;
    private boolean isActive;
    private List<TraineeDto> trainees;
}
