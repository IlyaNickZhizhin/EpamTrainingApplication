package org.epam.dto.trainerDto;

import jakarta.validation.constraints.NotBlank;
import org.epam.model.gymModel.TrainingType;

public class UpdateTrainerProfileRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private TrainingType.TrainingName specialization;
    private boolean isActive;
}
