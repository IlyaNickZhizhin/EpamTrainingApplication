package org.epam.gymservice.dto.reportDto;

import lombok.Data;
import org.epam.gymservice.dto.trainerDto.TrainerProfileResponse;

import java.util.Queue;

@Data
public class TrainerWorkloadResponse {
    String username;
    String firstName;
    String lastName;
    boolean isActive;
    Queue<TrainingSession> trainingSessions;

    public static TrainerWorkloadResponse of(TrainerProfileResponse trainer, String username, Queue<TrainingSession> session){
        TrainerWorkloadResponse response = new TrainerWorkloadResponse();
        response.setUsername(username);
        response.setFirstName(trainer.getFirstName());
        response.setLastName(trainer.getLastName());
        response.setActive(trainer.isActive());
        response.setTrainingSessions(session);
        return response;
    }
}
