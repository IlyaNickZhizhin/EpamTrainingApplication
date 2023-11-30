package org.epam.dto.trainingDto;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.epam.model.gymModel.TrainingType;

import java.time.LocalDate;

@Setter
@EqualsAndHashCode
public class TrainingDto {
    private int id;
    private String trainingName;
    private TrainingType.TrainingName trainingType;
    private LocalDate trainingDate;
    private double duration;
    private String opponentName;
}