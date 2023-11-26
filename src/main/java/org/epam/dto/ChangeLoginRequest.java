package org.epam.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeLoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;

}
