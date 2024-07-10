package org.epam.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
public class TrainerWorkloadRequest implements Serializable {
    String username;
    String firstName;
    String lastName;
    LocalDate trainingDate;
    double duration;
    boolean isActive;
}

