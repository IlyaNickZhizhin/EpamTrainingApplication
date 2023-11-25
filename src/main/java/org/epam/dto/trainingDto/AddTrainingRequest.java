package org.epam.dto.trainingDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.epam.model.gymModel.TrainingType;

import java.time.LocalDate;

public class AddTrainingRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String trainingName;
    @NotBlank
    private TrainingType.TrainingName trainingType;
    @NotNull
    private LocalDate trainingDate;
    @NotNull
    private double trainingDuration;
}

