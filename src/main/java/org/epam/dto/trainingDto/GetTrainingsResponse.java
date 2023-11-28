package org.epam.dto.trainingDto;

import lombok.Setter;

import java.util.List;
@Setter
public class GetTrainingsResponse {
    private List<ShortTrainingDto> trainings;
}
