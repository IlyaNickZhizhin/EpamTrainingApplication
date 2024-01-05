package org.epam.mainservice.dto.trainingDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateTraineeTrainerListRequest {
    @NotBlank
    private String traineeUsername;
    @NotEmpty
    private List<String> trainerUsernames;

}
