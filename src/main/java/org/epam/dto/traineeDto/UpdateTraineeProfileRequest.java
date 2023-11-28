package org.epam.dto.traineeDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateTraineeProfileRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    private LocalDate dateOfBirth;
    private String address;
    @NotNull
    private boolean isActive;
}
