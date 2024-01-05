package org.epam.mainservice.dto.trainingDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
public class GetTrainerTrainingsListRequest {
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String traineeName;
}
