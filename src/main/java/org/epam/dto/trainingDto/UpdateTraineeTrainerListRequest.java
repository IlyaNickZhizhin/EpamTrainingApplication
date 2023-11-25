package org.epam.dto.trainingDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class UpdateTraineeTrainerListRequest {
    @NotBlank
    private String traineeUsername;
    @NotEmpty
    private List<String> trainerUsernames;

}
