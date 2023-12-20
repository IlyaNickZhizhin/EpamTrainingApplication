package org.epam.dto.trainerDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.epam.dto.traineeDto.TraineeDto;
import org.epam.model.gymModel.TrainingType;

import java.util.List;
@Setter
@Getter
@EqualsAndHashCode
public class TrainerProfileResponse {
    private String firstName;
    private String lastName;
    private TrainingType.TrainingName specialization;
    private boolean isActive;
    private List<TraineeDto> trainees;
}

