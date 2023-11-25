package org.epam.dto;

import jakarta.validation.constraints.NotBlank;

public class ActivateDeactivateRequest {
    @NotBlank
    private String username;
    private boolean isActive;
}
