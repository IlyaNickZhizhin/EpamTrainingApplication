package org.epam.gymservice.config.feign;

import lombok.RequiredArgsConstructor;
import org.epam.common.dto.TrainerWorkloadRequest;
import org.epam.common.dto.TrainerWorkloadResponse;
import org.epam.common.dto.TrainingSession;
import org.epam.gymservice.dto.reportDto.GymTrainerWorkloadResponse;
import org.epam.gymservice.dto.trainerDto.TrainerProfileResponse;
import org.epam.gymservice.dto.trainingDto.GetTrainerTrainingsListRequest;
import org.epam.gymservice.dto.trainingDto.TrainingDto;
import org.epam.gymservice.service.TrainerService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

@Component
@RequiredArgsConstructor
public class ReportFeignClientFallback implements ReportFeignClient {

    private final TrainerService trainerService;

    @Override
    public TrainerWorkloadResponse addWorkload(String bearerToken, TrainerWorkloadRequest request) {
        return defaultFallback(request);
    }

    @Override
    public TrainerWorkloadResponse deleteWorkload(String bearerToken, TrainerWorkloadRequest request) {
        return defaultFallback(request);
    }

    private TrainerWorkloadResponse defaultFallback(TrainerWorkloadRequest request) {
        TrainerProfileResponse trainer = trainerService.selectByUsername(request.getUsername());
        List<TrainingDto> trainings = trainerService.getTrainerTrainingsList(request.getUsername(), new GetTrainerTrainingsListRequest()).getTrainings();
        Queue<TrainingSession> sessions = new PriorityQueue<>();
        sessions.addAll(trainings.stream().map(training -> TrainingSession.of(training.getTrainingDate(), training.getDuration())).toList());
        return GymTrainerWorkloadResponse.of(trainer, request.getUsername(), sessions);
    }
}
