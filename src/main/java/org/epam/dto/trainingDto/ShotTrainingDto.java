package org.epam.dto.trainingDto;

import lombok.Data;
import org.epam.model.gymModel.TrainingType;

import java.time.LocalDate;

@Data
public class ShotTrainingDto {
    private int id;
    private String trainingName;
    private TrainingType.TrainingName trainingType;
    private LocalDate trainingDate;
    private double duration;
    private String opponentName;
}
