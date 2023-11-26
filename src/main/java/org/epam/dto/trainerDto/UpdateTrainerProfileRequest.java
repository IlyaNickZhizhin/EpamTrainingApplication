package org.epam.dto.trainerDto;

import com.sun.istack.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.epam.model.gymModel.TrainingType;

@Data
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
