package org.epam.gymservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivateDeactivateRequest {
    @NotBlank
    private String username;
    private boolean isActive;
}
