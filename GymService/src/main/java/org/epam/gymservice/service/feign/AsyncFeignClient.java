package org.epam.gymservice.service.feign;

import lombok.RequiredArgsConstructor;
import org.epam.gymservice.config.feign.ReportFeignClient;
import org.epam.gymservice.dto.reportDto.TrainerWorkloadRequest;
import org.epam.gymservice.dto.reportDto.TrainerWorkloadResponse;
import org.epam.gymservice.dto.trainerDto.TrainerProfileResponse;
import org.epam.gymservice.dto.trainingDto.AddTrainingRequest;
import org.epam.gymservice.dto.trainingDto.GetTraineeTrainingsListRequest;
import org.epam.gymservice.dto.trainingDto.TrainingDto;
import org.epam.gymservice.service.TraineeService;
import org.epam.gymservice.service.TrainerService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AsyncFeignClient {
    private final ReportFeignClient feignClient;
    private final TrainerService trainerService;
    private final TraineeService traineeService;

    @Async
    public CompletableFuture<TrainerWorkloadResponse> addWorkload(String bearerToken, AddTrainingRequest request) {
        TrainerProfileResponse trainer = trainerService.selectByUsername(request.getTrainerUsername());
        return CompletableFuture
                .supplyAsync(()->feignClient.addWorkload(bearerToken, TrainerWorkloadRequest.of(trainer, request)));
    }

    @Async
    public void deleteAllWorkload(String bearerToken, String deletingTraineeUsername) {
        List<TrainingDto> trainingList = traineeService
                .getTraineeTrainingsList(deletingTraineeUsername, GetTraineeTrainingsListRequest.of(LocalDate.now()))
                .getTrainings();
        trainingList.forEach(training ->{
            TrainerProfileResponse trainer = trainerService.selectByUsername(training.getOpponentName());
            deleteWorkload(bearerToken, TrainerWorkloadRequest.of(trainer, training));
        });
    }

    @Async
    public CompletableFuture<TrainerWorkloadResponse> deleteWorkload(String bearerToken, TrainerWorkloadRequest request) {
        return CompletableFuture.supplyAsync(() -> feignClient.deleteWorkload(bearerToken, request));
    }

}
