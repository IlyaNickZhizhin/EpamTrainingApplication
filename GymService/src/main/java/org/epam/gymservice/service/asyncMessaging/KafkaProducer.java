package org.epam.gymservice.service.asyncMessaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.common.dto.TrainerWorkloadRequest;
import org.epam.gymservice.dto.reportDto.GymTrainerWorkloadRequest;
import org.epam.gymservice.dto.trainerDto.TrainerProfileResponse;
import org.epam.gymservice.dto.trainingDto.AddTrainingRequest;
import org.epam.gymservice.dto.trainingDto.GetTraineeTrainingsListRequest;
import org.epam.gymservice.dto.trainingDto.TrainingDto;
import org.epam.gymservice.service.TraineeService;
import org.epam.gymservice.service.TrainerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, TrainerWorkloadRequest> kafkaTemplate;
    private final KafkaTemplate<String, AddTrainingRequest> kafkaAddDLQTemplate;
    private final TrainerService trainerService;
    private final TraineeService traineeService;

    public void addWorkloadKafka(AddTrainingRequest request) {
        log.info("addWorkloadKafka take request for adding training with name: " + request.getTrainingName());
        try {
            kafkaTemplate.send("add-workload-topic",
                    GymTrainerWorkloadRequest.of(
                            trainerService.selectByUsername(request.getTrainerUsername()),
                            request));
        } catch (Exception e) {
            log.error("addWorkloadKafka error for adding training with name: {}", request.getTrainingName());
            kafkaAddDLQTemplate.send("add-workload-DLQ-topic", request);
        }
    }

    public void deleteAllWorkloadKafka(String deletingTraineeUsername) {
        log.info("deleting all training of trainee with name: " + deletingTraineeUsername);
        List<TrainingDto> trainingList = traineeService
                .getTraineeTrainingsList(deletingTraineeUsername, GetTraineeTrainingsListRequest.of(LocalDate.now()))
                .getTrainings();
        trainingList.forEach(training -> {
            TrainerProfileResponse trainer = trainerService.selectByUsername(training.getOpponentName());
            deleteWorkload(GymTrainerWorkloadRequest.of(trainer, training));
        });
    }

    public void deleteWorkload(TrainerWorkloadRequest request) {
        log.info("deleteWorkload kafka take request for deleting training on date: " + request.getTrainingDate());
        try {
            kafkaTemplate.send("delete-workload-topic", request);
        } catch (Exception e) {
            kafkaTemplate.send("delete-workload-DLQ-topic", request);
        }
    }

}
