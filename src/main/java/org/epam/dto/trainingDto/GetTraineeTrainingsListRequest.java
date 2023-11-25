package org.epam.dto.trainingDto;

import jakarta.validation.constraints.NotBlank;
import org.epam.model.gymModel.TrainingType;

import java.time.LocalDate;

public class GetTraineeTrainingsListRequest {
    @NotBlank
    private String username;
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String trainerName;
    private TrainingType.TrainingName trainingType;
}
