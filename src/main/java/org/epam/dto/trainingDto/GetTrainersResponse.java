package org.epam.dto.trainingDto;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.epam.dto.trainerDto.ShortTrainerDto;

import java.util.List;

@Setter
@EqualsAndHashCode
public class GetTrainersResponse {
    private List<ShortTrainerDto> trainers;
}
