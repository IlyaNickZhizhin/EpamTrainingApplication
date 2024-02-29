package org.epam.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TrainerWorkloadResponse implements Serializable {
    String username;
    String firstName;
    String lastName;
    boolean isActive;
    private List<TrainingSession> trainingSessions;
}
