package org.epam.dto.trainingDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.epam.dto.trainerDto.TrainerDto;

import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
public class GetTrainersResponse {
    private List<TrainerDto> trainers;
}
