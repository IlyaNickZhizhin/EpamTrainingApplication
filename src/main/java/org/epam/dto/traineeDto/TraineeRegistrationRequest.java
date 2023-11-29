package org.epam.dto.traineeDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TraineeRegistrationRequest {
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    private LocalDate dateOfBirth;
    private String address;
}
