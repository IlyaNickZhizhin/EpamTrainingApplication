package org.epam.dto.trainerDto;

import jakarta.validation.constraints.NotBlank;

public class ActivateDeactivateTrainerRequest {
    @NotBlank
    private String username;
    private boolean isActive;
}
