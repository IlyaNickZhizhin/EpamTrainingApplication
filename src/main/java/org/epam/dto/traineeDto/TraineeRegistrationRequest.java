package org.epam.dto.traineeDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TraineeRegistrationRequest {
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    private LocalDate dateOfBirth;
    private String address;
}
