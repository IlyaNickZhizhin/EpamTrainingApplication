package org.epam.dto.trainingDto;

import lombok.Setter;
import org.epam.model.gymModel.TrainingType;

import java.util.List;

@Setter
public class GetTrainingTypesResponse {
    private List<TrainingType> trainingTypes;

}
