package org.epam.dto.trainerDto;

import lombok.Data;
import org.epam.dto.traineeDto.ShotTraineeDto;
import org.epam.model.gymModel.TrainingType;

import java.util.List;
@Data
public class TrainerProfileResponse {
    private String firstName;
    private String lastName;
    private TrainingType.TrainingName specialization;
    private boolean isActive;
    private List<ShotTraineeDto> trainees;
}

