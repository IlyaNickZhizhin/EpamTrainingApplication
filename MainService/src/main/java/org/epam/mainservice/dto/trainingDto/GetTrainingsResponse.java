package org.epam.mainservice.dto.trainingDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@EqualsAndHashCode
public class GetTrainingsResponse {
    private List<TrainingDto> trainings;
}
