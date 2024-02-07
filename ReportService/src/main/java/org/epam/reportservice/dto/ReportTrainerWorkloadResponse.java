package org.epam.reportservice.dto;

import org.epam.common.dto.TrainerWorkloadResponse;
import org.epam.common.dto.TrainingSession;
import org.epam.reportservice.model.TrainerKey;

import java.util.Queue;

public class ReportTrainerWorkloadResponse extends TrainerWorkloadResponse {

    public static ReportTrainerWorkloadResponse of(TrainerKey key, Queue<TrainingSession> session){
        ReportTrainerWorkloadResponse response = new ReportTrainerWorkloadResponse();
        response.setUsername(key.getUsername());
        response.setFirstName(key.getFirstName());
        response.setLastName(key.getLastName());
        response.setActive(key.isActive());
        response.setTrainingSessions(session);
        return response;
    }
}
