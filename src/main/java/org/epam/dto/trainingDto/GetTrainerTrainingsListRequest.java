package org.epam.dto.trainingDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GetTrainerTrainingsListRequest {
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String traineeName;
}
