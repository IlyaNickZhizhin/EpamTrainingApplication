package org.epam.gymservice.dto.trainingDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.epam.gymservice.dto.trainerDto.TrainerDto;

import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class GetTrainersResponse {
    private List<TrainerDto> trainers;
}
