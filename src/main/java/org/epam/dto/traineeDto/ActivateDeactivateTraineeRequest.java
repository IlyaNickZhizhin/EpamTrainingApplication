package org.epam.dto.traineeDto;

import jakarta.validation.constraints.NotBlank;

public class ActivateDeactivateTraineeRequest {
    @NotBlank
    private String username;
    private boolean isActive;
}

