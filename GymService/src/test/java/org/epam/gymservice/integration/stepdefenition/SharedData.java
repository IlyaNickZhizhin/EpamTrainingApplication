package org.epam.gymservice.integration.stepdefenition;

import org.epam.gymservice.dto.trainingDto.GetTrainingsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class SharedData {
    ResponseEntity<Boolean> passwordChangeResponse;
    ResponseEntity<GetTrainingsResponse> trainingsResponse;
}
