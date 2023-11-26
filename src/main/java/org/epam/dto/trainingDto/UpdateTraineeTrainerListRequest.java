package org.epam.dto.trainingDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateTraineeTrainerListRequest {
    @NotBlank
    private String traineeUsername;
    @NotEmpty
    private List<String> trainerUsernames;

}
