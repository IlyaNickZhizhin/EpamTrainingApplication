package org.epam.dto.trainerDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import org.epam.model.gymModel.TrainingType;


public class TrainerRegistrationRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private TrainingType.TrainingName specialization;
}

