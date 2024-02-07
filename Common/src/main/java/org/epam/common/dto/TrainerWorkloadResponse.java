package org.epam.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Queue;

@Data
public class TrainerWorkloadResponse implements Serializable {
    String username;
    String firstName;
    String lastName;
    boolean isActive;
    Queue<TrainingSession> trainingSessions;
}
