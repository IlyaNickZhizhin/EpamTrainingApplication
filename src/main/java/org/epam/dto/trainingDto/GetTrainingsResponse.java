package org.epam.dto.trainingDto;

import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.util.List;
@Setter
@EqualsAndHashCode
public class GetTrainingsResponse {
    private List<ShortTrainingDto> trainings;
}
