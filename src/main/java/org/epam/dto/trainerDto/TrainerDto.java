package org.epam.dto.trainerDto;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.epam.model.gymModel.TrainingType;

@Setter
@EqualsAndHashCode
public class TrainerDto {
    private String firstname;
    private String lastname;
    private String username;
    private TrainingType.TrainingName specialization;
}
