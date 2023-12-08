package org.epam.dto.trainingDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.epam.model.gymModel.TrainingType;

import java.time.LocalDate;

@Getter
@Setter
public class GetTraineeTrainingsListRequest {
    @NotBlank
    private String username;
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String trainerName;
    private TrainingType.TrainingName trainingType;
}
