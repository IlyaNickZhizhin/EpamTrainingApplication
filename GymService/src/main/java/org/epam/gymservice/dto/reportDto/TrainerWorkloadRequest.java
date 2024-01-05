package org.epam.gymservice.dto.reportDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TrainerWorkloadRequest {
    @NotBlank
    String username;
    @NotBlank
    String firstName;
    @NotBlank
    String lastName;
    boolean isActive;
    @NotBlank
    LocalDate trainingDate;
    @NotBlank
    double duration;
    @NotBlank
    ActionType actionType;

    public enum ActionType{
        ADD, DELETE
    }
}
