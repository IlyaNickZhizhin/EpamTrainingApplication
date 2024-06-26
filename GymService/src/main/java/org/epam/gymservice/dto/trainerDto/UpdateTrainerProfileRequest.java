package org.epam.gymservice.dto.trainerDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.epam.gymservice.model.gymModel.TrainingType;

@Getter
@Setter
public class UpdateTrainerProfileRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private TrainingType.TrainingName specialization;
    @NotNull
    private boolean isActive;
}
