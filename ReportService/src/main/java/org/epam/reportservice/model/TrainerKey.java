package org.epam.reportservice.model;

import lombok.Data;
import org.epam.reportservice.dto.TrainerWorkloadRequest;

@Data
public class TrainerKey {
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;

    public static TrainerKey of(String username, String firstName, String lastName, boolean isActive) {
        TrainerKey trainerKey = new TrainerKey();
        trainerKey.username = username;
        trainerKey.firstName = firstName;
        trainerKey.lastName = lastName;
        trainerKey.isActive = isActive;
        return trainerKey;
    }
    public static TrainerKey of(TrainerWorkloadRequest request) {
        TrainerKey trainerKey = new TrainerKey();
        trainerKey.username = request.getUsername();
        trainerKey.firstName = request.getFirstName();
        trainerKey.lastName = request.getLastName();
        trainerKey.isActive = request.isActive();
        return trainerKey;
    }
}
