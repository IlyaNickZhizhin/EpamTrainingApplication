package org.epam.dto.trainerDto;

import com.sun.istack.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.epam.model.gymModel.TrainingType;

@Getter
@Setter
public class UpdateTrainerProfileRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    private TrainingType.TrainingName specialization;
    @NotNull
    private boolean isActive;
}
