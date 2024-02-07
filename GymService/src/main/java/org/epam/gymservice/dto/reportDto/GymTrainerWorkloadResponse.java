package org.epam.gymservice.dto.reportDto;

import org.epam.common.dto.TrainerWorkloadResponse;
import org.epam.common.dto.TrainingSession;
import org.epam.gymservice.dto.trainerDto.TrainerProfileResponse;

import java.util.Queue;

public class GymTrainerWorkloadResponse extends TrainerWorkloadResponse {

    public static TrainerWorkloadResponse of(TrainerProfileResponse trainer, String username, Queue<TrainingSession> session){
        GymTrainerWorkloadResponse response = new GymTrainerWorkloadResponse();
        response.setUsername(username);
        response.setFirstName(trainer.getFirstName());
        response.setLastName(trainer.getLastName());
        response.setActive(trainer.isActive());
        response.setTrainingSessions(session);
        return response;
    }
}
