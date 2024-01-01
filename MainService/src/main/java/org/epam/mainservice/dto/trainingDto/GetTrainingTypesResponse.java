package org.epam.mainservice.dto.trainingDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.epam.mainservice.model.gymModel.TrainingType;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class GetTrainingTypesResponse {
    private List<TrainingType> trainingTypes;
}
