package org.epam.dto.trainingDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.epam.model.gymModel.TrainingType;

import java.time.LocalDate;

public class AddTrainingRequest {
    @NotBlank
    private String traineeUsername;
    @NotBlank
    private String trainerUsername;
    @NotBlank
    private String trainingName;
    @NotNull
    private LocalDate trainingDate;
    @NotNull
    private double trainingDuration;
}

