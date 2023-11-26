package org.epam.dto.trainerDto;

import lombok.Data;
import org.epam.model.gymModel.TrainingType;

@Data
public class ShotTrainerDto {
    private String firstname;
    private String lastname;
    private String username;
    private TrainingType.TrainingName specialization;
}
