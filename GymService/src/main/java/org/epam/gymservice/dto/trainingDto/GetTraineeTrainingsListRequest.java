package org.epam.gymservice.dto.trainingDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.epam.gymservice.model.gymModel.TrainingType;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
public class GetTraineeTrainingsListRequest {
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String trainerName;
    private TrainingType.TrainingName trainingType;

    public static GetTraineeTrainingsListRequest of(LocalDate periodFrom){
        GetTraineeTrainingsListRequest request = new GetTraineeTrainingsListRequest();
        request.setPeriodFrom(periodFrom);
        return request;
    }
}
