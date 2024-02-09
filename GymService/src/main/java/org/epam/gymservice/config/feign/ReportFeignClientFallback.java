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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        Map<LocalDate, Double> trainindMap = trainings.stream().collect(
                Collectors.toMap(
                        TrainingDto::getTrainingDate,
                        TrainingDto::getDuration,
                        Double::sum));
        Set<TrainingSession> sessions = TrainingSession.setOf(trainindMap);
        return GymTrainerWorkloadResponse.of(trainer, request.getUsername(), new ArrayList<>(sessions));
    }
}
