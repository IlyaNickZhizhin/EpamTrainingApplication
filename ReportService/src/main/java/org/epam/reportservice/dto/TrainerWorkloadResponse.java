package org.epam.reportservice.dto;

import lombok.Data;

import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

@Data
public class TrainerWorkloadResponse {
    String username;
    String firstName;
    String lastName;
    boolean isActive;
    Map<Year, Map<Month, Double>> map = new HashMap<>();
}
