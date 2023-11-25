package org.epam.dto.trainerDto;

import org.epam.dto.traineeDto.ShotTraineeDto;
import org.epam.dto.traineeDto.TraineeDto;
import org.epam.model.gymModel.TrainingType;

import java.util.List;

public class TrainerProfileResponse {
    private String firstName;
    private String lastName;
    private TrainingType.TrainingName specialization;
    private boolean isActive;
    private List<ShotTraineeDto> trainees;
}

