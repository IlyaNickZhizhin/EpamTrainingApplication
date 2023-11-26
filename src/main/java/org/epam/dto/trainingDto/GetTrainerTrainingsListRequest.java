package org.epam.dto.trainingDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetTrainerTrainingsListRequest {
    @NotBlank
    private String username;
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String traineeName;

}
