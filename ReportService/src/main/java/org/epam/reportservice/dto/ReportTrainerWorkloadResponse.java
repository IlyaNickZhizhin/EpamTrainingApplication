package org.epam.reportservice.dto;

import org.epam.common.dto.TrainerWorkloadResponse;
import org.epam.reportservice.model.Workload;

import java.util.ArrayList;
public class ReportTrainerWorkloadResponse extends TrainerWorkloadResponse {

    public static ReportTrainerWorkloadResponse of(Workload workload){
        ReportTrainerWorkloadResponse response = new ReportTrainerWorkloadResponse();
        response.setUsername(workload.getUsername());
        response.setFirstName(workload.getFirstName());
        response.setLastName(workload.getLastName());
        response.setActive(workload.isActive());
        response.setTrainingSessions(new ArrayList<>(workload.getTrainingSessions()));
        return response;
    }
}
