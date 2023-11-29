package org.epam.dto.trainingDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GetTrainerTrainingsListRequest {
    @NotBlank
    private String username;
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String traineeName;
}
