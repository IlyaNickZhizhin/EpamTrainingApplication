package org.epam.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActivateDeactivateRequest {
    @NotBlank
    private String username;
    private boolean isActive;
}
