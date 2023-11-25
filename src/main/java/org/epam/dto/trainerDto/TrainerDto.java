package org.epam.dto.trainerDto;

import lombok.Data;
import org.epam.model.gymModel.TrainingType;


@Data
public class TrainerDto {
    private int id;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private boolean isActive;
    private TrainingType.TrainingName specialization;
}
