package org.epam.dto;

import lombok.Data;
import org.epam.model.gymModel.TrainingType;

import java.time.LocalDate;

@Data
public class TrainingDto {
    private int id;
    private String trainingName;
    private TrainingType.TrainingName trainingType;
    private LocalDate trainingDate;
    private double duration;
    private TrainerDto trainer;
    private TraineeDto trainee;
}
