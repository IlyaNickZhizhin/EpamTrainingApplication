package org.epam.reportservice.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epam.reportservice.model.TrainerKey;
import org.epam.reportservice.model.TrainingSession;

import java.util.Queue;

@Data
@EqualsAndHashCode
public class TrainerWorkloadResponse {
    String username;
    String firstName;
    String lastName;
    boolean isActive;
    Queue<TrainingSession> trainingSessions;

    public static TrainerWorkloadResponse of(TrainerKey key, Queue<TrainingSession> session){
        TrainerWorkloadResponse response = new TrainerWorkloadResponse();
        response.setUsername(key.getUsername());
        response.setFirstName(key.getFirstName());
        response.setLastName(key.getLastName());
        response.setActive(key.isActive());
        response.setTrainingSessions(session);
        return response;
    }
}
