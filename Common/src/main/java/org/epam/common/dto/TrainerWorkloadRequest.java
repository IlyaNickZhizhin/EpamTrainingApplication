package org.epam.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class TrainerWorkloadRequest implements Serializable {
    String username;
    String firstName;
    String lastName;
    boolean isActive;
    LocalDate trainingDate;
    double duration;
}

