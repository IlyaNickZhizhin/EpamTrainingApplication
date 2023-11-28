package org.epam.dto.trainingDto;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.epam.model.gymModel.TrainingType;

import java.util.List;

@Setter
@EqualsAndHashCode
public class GetTrainingTypesResponse {
    private List<TrainingType> trainingTypes;
}
