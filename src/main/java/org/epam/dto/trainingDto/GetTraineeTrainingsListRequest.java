package org.epam.dto.trainingDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.epam.model.gymModel.TrainingType;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
public class GetTraineeTrainingsListRequest {
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String trainerName;
    private TrainingType.TrainingName trainingType;
}
