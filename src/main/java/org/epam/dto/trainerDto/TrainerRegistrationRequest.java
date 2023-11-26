package org.epam.dto.trainerDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.epam.model.gymModel.TrainingType;

@Data
public class TrainerRegistrationRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private TrainingType.TrainingName specialization;

    public String getFirstname() {
        return firstName;
    }

    public String getLastname() {
        return lastName;
    }
}

