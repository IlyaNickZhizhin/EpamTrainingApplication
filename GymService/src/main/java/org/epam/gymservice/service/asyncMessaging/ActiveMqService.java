package org.epam.gymservice.service.asyncMessaging;

import lombok.RequiredArgsConstructor;
import org.epam.gymservice.dto.reportDto.TrainerWorkloadRequest;
import org.epam.gymservice.dto.trainerDto.TrainerProfileResponse;
import org.epam.gymservice.dto.trainingDto.AddTrainingRequest;
import org.epam.gymservice.dto.trainingDto.GetTraineeTrainingsListRequest;
import org.epam.gymservice.dto.trainingDto.TrainingDto;
import org.epam.gymservice.service.TraineeService;
import org.epam.gymservice.service.TrainerService;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActiveMqService {
    private final JmsTemplate jmsTemplate;
    private final TrainerService trainerService;
    private final TraineeService traineeService;

    public void addWorkload(String bearerToken, AddTrainingRequest request) {
        TrainerProfileResponse trainer = trainerService.selectByUsername(request.getTrainerUsername());
        jmsTemplate.convertAndSend("addTrainingRequestQueue", TrainerWorkloadRequest.of(trainer, request));
    }

    public void deleteAllWorkload(String bearerToken, String deletingTraineeUsername) {
        List<TrainingDto> trainingList = traineeService
                .getTraineeTrainingsList(deletingTraineeUsername, GetTraineeTrainingsListRequest.of(LocalDate.now()))
                .getTrainings();
        trainingList.forEach(training ->{
            TrainerProfileResponse trainer = trainerService.selectByUsername(training.getOpponentName());
            deleteWorkload(bearerToken, TrainerWorkloadRequest.of(trainer, training));
        });
    }

    public void deleteWorkload(String bearerToken, TrainerWorkloadRequest request) {
        jmsTemplate.convertAndSend("deleteTrainingRequestQueue", request);
    }

}
