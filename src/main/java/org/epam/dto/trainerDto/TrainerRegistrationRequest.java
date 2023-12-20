package org.epam.dto.trainerDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.epam.model.gymModel.TrainingType;

@Getter
@Setter
public class TrainerRegistrationRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private TrainingType.TrainingName specialization;
}

