package org.epam.mainservice.dto.trainerDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.epam.mainservice.model.gymModel.TrainingType;

@Setter
@Getter
@EqualsAndHashCode
public class TrainerDto {
    private String firstName;
    private String lastName;
    private String username;
    private TrainingType.TrainingName specialization;
}
