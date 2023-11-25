package org.epam.dto.traineeDto;

import jakarta.validation.constraints.NotBlank;

public class DeleteTraineeProfileRequest {
    @NotBlank
    private String username;
}

