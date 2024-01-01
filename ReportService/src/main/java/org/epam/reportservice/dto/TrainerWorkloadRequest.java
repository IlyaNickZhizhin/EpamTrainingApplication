package org.epam.reportservice.dto;

import lombok.Data;
import org.epam.reportservice.model.ActionType;

import java.time.LocalDate;

@Data
public class TrainerWorkloadRequest {
    String username;
    String firstName;
    String lastName;
    boolean isActive;
    LocalDate trainingDate;
    double duration;
    ActionType actionType;
}
