package org.epam.gymservice.service.asyncMessaging;

import org.epam.common.dto.TrainerWorkloadRequest;
import org.epam.gymservice.dto.trainingDto.AddTrainingRequest;

public interface WorkloadsSender {
    void addWorkload(AddTrainingRequest request);
    void deleteWorkload(TrainerWorkloadRequest request);
    void deleteAllWorkload(String deletingTraineeUsername);
}
