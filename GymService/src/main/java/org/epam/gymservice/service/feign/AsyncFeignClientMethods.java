package org.epam.gymservice.service.feign;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import org.epam.gymservice.config.ReportFeignClient;
import org.epam.gymservice.dto.reportDto.TrainerWorkloadRequest;
import org.epam.gymservice.dto.reportDto.TrainerWorkloadResponse;
import org.epam.gymservice.dto.reportDto.TrainingSession;
import org.epam.gymservice.dto.trainerDto.TrainerProfileResponse;
import org.epam.gymservice.dto.trainingDto.AddTrainingRequest;
import org.epam.gymservice.dto.trainingDto.GetTraineeTrainingsListRequest;
import org.epam.gymservice.dto.trainingDto.GetTrainerTrainingsListRequest;
import org.epam.gymservice.dto.trainingDto.TrainingDto;
import org.epam.gymservice.service.TraineeService;
import org.epam.gymservice.service.TrainerService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AsyncFeignClientMethods {
    private final ReportFeignClient feignClient;
    private final TrainerService trainerService;
    private final TraineeService traineeService;

    @Async
    @HystrixCommand(fallbackMethod = "fallbackWorkloadChange")
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
    @HystrixCommand(fallbackMethod = "fallbackWorkloadChange")
    public CompletableFuture<TrainerWorkloadResponse> deleteWorkload(String bearerToken, TrainerWorkloadRequest request) {
        return CompletableFuture.supplyAsync(() -> feignClient.deleteWorkload(bearerToken, request));
    }

    public CompletableFuture<TrainerWorkloadResponse> fallbackWorkloadChange(String bearerToken, AddTrainingRequest request){
        TrainerProfileResponse trainer = trainerService.selectByUsername(request.getTrainerUsername());
        List<TrainingDto> trainings = trainerService.getTrainerTrainingsList(request.getTrainerUsername(), new GetTrainerTrainingsListRequest()).getTrainings();
        Queue<TrainingSession> sessions = new PriorityQueue<>();
        sessions.addAll(trainings.stream().map(training -> TrainingSession.of(training.getTrainingDate(), training.getDuration())).toList());
        return CompletableFuture.completedFuture(TrainerWorkloadResponse.of(trainer, request.getTrainerUsername(), sessions));
    }

}
