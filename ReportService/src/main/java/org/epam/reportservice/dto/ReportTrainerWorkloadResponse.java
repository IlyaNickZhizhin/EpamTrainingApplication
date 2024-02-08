package org.epam.reportservice.dto;

import lombok.Data;
import org.epam.common.dto.TrainerWorkloadResponse;
import org.epam.common.dto.TrainingSession;
import org.epam.reportservice.model.TrainerKey;
import org.epam.reportservice.model.Workload;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Data
public class ReportTrainerWorkloadResponse extends TrainerWorkloadResponse {

    private List<org.epam.reportservice.model.TrainingSession> trainingSessionList;


    public static ReportTrainerWorkloadResponse of(TrainerKey key, Queue<TrainingSession> session){
        ReportTrainerWorkloadResponse response = new ReportTrainerWorkloadResponse();
        response.setUsername(key.getUsername());
        response.setFirstName(key.getFirstName());
        response.setLastName(key.getLastName());
        response.setActive(key.isActive());
        response.setTrainingSessions(session);
        return response;
    }


    public static ReportTrainerWorkloadResponse of(Workload workload){
        ReportTrainerWorkloadResponse response = new ReportTrainerWorkloadResponse();
        response.setUsername(workload.getUsername());
        response.setFirstName(workload.getFirstName());
        response.setLastName(workload.getLastName());
        response.setActive(workload.isActive());
        response.setTrainingSessionList(new ArrayList<>(workload.getTrainingSessions()));
        return response;
    }
}
