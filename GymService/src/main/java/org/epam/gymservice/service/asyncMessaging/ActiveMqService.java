package org.epam.gymservice.service.asyncMessaging;

import lombok.RequiredArgsConstructor;
import org.apache.activemq.command.ActiveMQQueue;
import org.epam.common.dto.TrainerWorkloadRequest;
import org.epam.gymservice.dto.reportDto.GymTrainerWorkloadRequest;
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

    public void addWorkload(AddTrainingRequest request) {
        TrainerProfileResponse trainer = trainerService.selectByUsername(request.getTrainerUsername());
        try {
            jmsTemplate.convertAndSend("addTrainingRequestQueue",
                    GymTrainerWorkloadRequest.of(trainer, request),
                    message -> {
                        message.setJMSReplyTo(new ActiveMQQueue("trainersWorkloads"));
                        message.setJMSExpiration(5000);
                        return message;
                    });
        } catch (Exception e) {
            jmsTemplate.convertAndSend("DLQ.addTrainingRequestQueue", request);
        }
    }

    public void deleteAllWorkload(String deletingTraineeUsername) {
        List<TrainingDto> trainingList = traineeService
                .getTraineeTrainingsList(deletingTraineeUsername, GetTraineeTrainingsListRequest.of(LocalDate.now()))
                .getTrainings();
        trainingList.forEach(training ->{
            TrainerProfileResponse trainer = trainerService.selectByUsername(training.getOpponentName());
            deleteWorkload(GymTrainerWorkloadRequest.of(trainer, training));
        });
    }

    public void deleteWorkload(TrainerWorkloadRequest request) {
        try {
        jmsTemplate.convertAndSend("deleteTrainingRequestQueue", request,
                message -> { message.setJMSReplyTo(new ActiveMQQueue("trainersWorkloads"));
                    message.setJMSExpiration(5000);
                    return message;
                });
        } catch (Exception e) {
            jmsTemplate.convertAndSend("DLQ.addTrainingRequestQueue", request);
        }
    }

}
