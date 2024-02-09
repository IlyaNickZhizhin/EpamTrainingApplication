package org.epam.reportservice.model;

import lombok.Data;
import org.epam.common.dto.TrainerWorkloadRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Data
@Document(collection = "gymWorkloads")
public class Workload {
    @Id
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private Set<TrainingSession> trainingSessions;

    public static Workload of(TrainerWorkloadRequest request){
        Workload workload = new Workload();
        workload.setUsername(request.getUsername());
        workload.setFirstName(request.getFirstName());
        workload.setLastName(request.getLastName());
        workload.setActive(request.isActive());
        SortedSet<TrainingSession> sessions = new TreeSet<>();
        workload.setTrainingSessions(sessions);
        return workload;
    }


}
