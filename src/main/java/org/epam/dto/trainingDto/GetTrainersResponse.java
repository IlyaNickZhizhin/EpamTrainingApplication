package org.epam.dto.trainingDto;

import lombok.Setter;
import org.epam.dto.trainerDto.ShortTrainerDto;

import java.util.List;

@Setter
public class GetTrainersResponse {
    private List<ShortTrainerDto> trainers;
}
