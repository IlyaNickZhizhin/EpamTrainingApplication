package org.epam.gymservice.dto.trainingDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.epam.gymservice.model.gymModel.TrainingType;

import java.time.LocalDate;
@Getter
@Setter
@EqualsAndHashCode
public class AddTrainingRequest {
    @NotBlank
    private String traineeUsername;
    @NotBlank
    private String trainerUsername;
    @NotBlank
    private String trainingName;
    @NotNull
    private TrainingType.TrainingName trainingType;
    @NotNull
    private LocalDate trainingDate;
    @NotNull
    private double trainingDuration;
}

