package org.epam.mainservice.dto.reportDto;

import lombok.Data;

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

    enum ActionType{
        ADD, DELETE
    }
}
