package org.epam.gymservice.dto.reportDto;

import org.epam.common.dto.TrainerWorkloadRequest;
import org.epam.gymservice.dto.trainerDto.TrainerProfileResponse;
import org.epam.gymservice.dto.trainingDto.AddTrainingRequest;
import org.epam.gymservice.dto.trainingDto.TrainingDto;

import java.io.Serializable;

public class GymTrainerWorkloadRequest extends TrainerWorkloadRequest implements Serializable {

    public static TrainerWorkloadRequest of(TrainerProfileResponse trainer, AddTrainingRequest request) {
        TrainerWorkloadRequest request1 = new TrainerWorkloadRequest();
        request1.setUsername(request.getTrainerUsername());
        request1.setFirstName(trainer.getFirstName());
        request1.setLastName(trainer.getLastName());
        request1.setActive(trainer.isActive());
        request1.setTrainingDate(request.getTrainingDate());
        request1.setDuration(request.getTrainingDuration());
        return request1;
    }

    public static TrainerWorkloadRequest of(TrainerProfileResponse trainer, TrainingDto training) {
        TrainerWorkloadRequest request = new TrainerWorkloadRequest();
        request.setUsername(training.getOpponentName());
        request.setFirstName(trainer.getFirstName());
        request.setLastName(trainer.getLastName());
        request.setActive(trainer.isActive());
        request.setTrainingDate(training.getTrainingDate());
        request.setDuration(training.getDuration());
        return request;
    }
}
