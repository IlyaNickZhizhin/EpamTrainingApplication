package org.epam.dto.trainerDto;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.epam.dto.traineeDto.ShortTraineeDto;
import org.epam.model.gymModel.TrainingType;

import java.util.List;
@Setter
@EqualsAndHashCode
public class TrainerProfileResponse {
    private String firstName;
    private String lastName;
    private TrainingType.TrainingName specialization;
    private boolean isActive;
    private List<ShortTraineeDto> trainees;
}

