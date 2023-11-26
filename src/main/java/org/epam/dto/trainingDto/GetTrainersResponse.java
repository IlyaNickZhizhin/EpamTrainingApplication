package org.epam.dto.trainingDto;

import lombok.Setter;
import org.epam.dto.trainerDto.ShotTrainerDto;

import java.util.List;

@Setter
public class GetTrainersResponse {
    private List<ShotTrainerDto> trainers;
}
