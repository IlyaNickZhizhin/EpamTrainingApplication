package org.epam.gymservice.service.asyncMessaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.epam.common.dto.TrainerWorkloadRequest;
import org.epam.gymservice.dto.reportDto.GymTrainerWorkloadRequest;
import org.epam.gymservice.dto.trainerDto.TrainerProfileResponse;
import org.epam.gymservice.dto.trainingDto.AddTrainingRequest;
import org.epam.gymservice.dto.trainingDto.GetTraineeTrainingsListRequest;
import org.epam.gymservice.dto.trainingDto.TrainingDto;
import org.epam.gymservice.service.TraineeService;
import org.epam.gymservice.service.TrainerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Profile("cloud")
public class SQSService implements WorkloadsSender {
    @Value("${spring.aws.brokerUrl}")
    private String brokerUrl;
    private final SqsClient sqsClient;
    private final ObjectMapper mapper;
    private final TrainerService trainerService;
    private final TraineeService traineeService;

    public void addWorkload(AddTrainingRequest request) {
        TrainerProfileResponse trainer = trainerService.selectByUsername(request.getTrainerUsername());
        try {
            String messageBody = mapper.writeValueAsString(GymTrainerWorkloadRequest.of(trainer, request));
            Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
            messageAttributes.put("MessageType", MessageAttributeValue.builder()
                    .stringValue("addWorkload").dataType("String").build());
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder().queueUrl(brokerUrl)
                    .messageBody(messageBody)
                    .messageAttributes(messageAttributes)
                    .build();
            sqsClient.sendMessage(sendMessageRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
            String messageBody = mapper.writeValueAsString(request);
            Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
            messageAttributes.put("MessageType", MessageAttributeValue.builder()
                    .stringValue("deleteWorkload").dataType("String").build());
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder().queueUrl(brokerUrl)
                    .messageBody(messageBody)
                    .messageAttributes(messageAttributes)
                    .build();
            sqsClient.sendMessage(sendMessageRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
