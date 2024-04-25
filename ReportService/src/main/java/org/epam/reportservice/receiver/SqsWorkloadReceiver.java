package org.epam.reportservice.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.common.dto.TrainerWorkloadRequest;
import org.epam.common.dto.TrainerWorkloadResponse;
import org.epam.reportservice.service.WorkloadCloudService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("cloud")
@EnableScheduling
public class SqsWorkloadReceiver{

    private final WorkloadCloudService workloadService;
    private final SqsClient sqsClient;
    @Value("${spring.aws.brokerUrl}")
    private String brokerURL;
    private final ObjectMapper objectMapper;

    @Transactional
    @Scheduled(fixedDelay = 1000)
    public TrainerWorkloadResponse receiveMessage() {
        log.info("Pooling for additional workload");
        TrainerWorkloadResponse response = new TrainerWorkloadResponse();
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest
                .builder()
                .queueUrl(brokerURL)
                .maxNumberOfMessages(1)
                .messageAttributeNames("All")
                .waitTimeSeconds(10)
                .build();
        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
        if (!messages.isEmpty()) {
            if(!messages.get(0).hasMessageAttributes() || (
                    messages.get(0).messageAttributes().get("MessageType").stringValue().startsWith("add") &&
                    messages.get(0).messageAttributes().get("MessageType").stringValue().startsWith("delete"))) {
                log.warn("No message attributes or message attributes are not valid in message#"
                        + messages.get(0).messageId() + " it was deleted");
                sqsClient.deleteMessage(DeleteMessageRequest.builder()
                        .queueUrl(brokerURL).receiptHandle(messages.get(0).receiptHandle())
                        .build());
                return response;
            }
            Message message = messages.get(0);
            if (message.messageAttributes().get("MessageType").stringValue().startsWith("add")) {
                response = getTrainerWorkloadResponse(message, true);
            } else {
                response = getTrainerWorkloadResponse(message, false);
            }
        } else log.info("Nothing founded");
        return response;
    }

    private TrainerWorkloadResponse getTrainerWorkloadResponse(Message message, boolean isAdd) {
        log.info("Founded workload for " + (isAdd ? "ADD":"DELETE"));
        try {
            TrainerWorkloadRequest request = objectMapper.readValue(message.body(), TrainerWorkloadRequest.class);
            TrainerWorkloadResponse response = isAdd ? workloadService.addWorkload(request) : workloadService.deleteWorkload(request);
            sqsClient.deleteMessage(DeleteMessageRequest.builder()
                    .queueUrl(brokerURL).receiptHandle(message.receiptHandle())
                    .build());
            log.info("Workload of trainer: " + (response==null ? "NONAME" : response.getFirstName())
                    + (isAdd ? "ADDED":"DELETED"));
            return response;
        } catch (JsonProcessingException e) {
            log.error("Exception in parsing of message#" + message.messageId());
            throw new RuntimeException("Parsing error in message #" + message.messageId());
        }
    }
}
