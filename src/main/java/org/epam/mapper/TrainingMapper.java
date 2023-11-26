package org.epam.mapper;

import org.epam.dto.trainingDto.ShotTrainingDto;
import org.epam.dto.trainingDto.TrainingDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface TrainingMapper<M> {
    @Mapping(target = "opponentName", source = "trainer.firstname")
    List<ShotTrainingDto> toShotTrainingDtoListForTrainee(List<TrainingDto> trainings);

    @Mapping(target = "opponentName", source = "trainee.firstname")
    List<ShotTrainingDto> toShotTrainingDtoListForTrainer(List<TrainingDto> trainings);
}
