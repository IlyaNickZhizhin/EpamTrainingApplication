package org.epam.dto.traineeDto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class UpdateTraineeProfileRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
}
