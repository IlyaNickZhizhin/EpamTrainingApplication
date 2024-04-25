package org.epam.reportservice.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.epam.common.dto.TrainerWorkloadRequest;
import org.epam.common.dto.TrainerWorkloadResponse;
import org.epam.reportservice.Reader;
import org.epam.reportservice.dto.ReportTrainerWorkloadRequest;
import org.epam.reportservice.dto.ReportTrainerWorkloadResponse;
import org.epam.reportservice.service.WorkloadCloudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SqsWorkloadReceiverTest {

    @Mock
    private WorkloadCloudService workloadService;
    @Mock
    private SqsClient sqsClient;
    @Mock
    private ObjectMapper objectMapper;
    @Value("${spring.aws.brokerUrl}")
    private String brokerURL;
    @InjectMocks
    private SqsWorkloadReceiver sqsWorkloadReceiver;

    private TrainerWorkloadRequest request;
    Reader reader = new Reader();
    ReceiveMessageRequest addRequest;
    Message messageAdd;
    Message messageDelete;
    ReceiveMessageResponse messageResponseAdd;
    ReceiveMessageResponse messageResponseDelete;

    @BeforeEach
    void setUp() {
        request = reader
                .readEntity("src/test/resources/models/workloads/workload1.json", ReportTrainerWorkloadRequest.class);
        addRequest = ReceiveMessageRequest
                .builder()
                .queueUrl(brokerURL)
                .maxNumberOfMessages(1)
                .waitTimeSeconds(10)
                .messageAttributeNames("All")
                .build();
        Map<String, MessageAttributeValue> messageAttributesAdd = new HashMap<>();
        Map<String, MessageAttributeValue> messageAttributesDelete = new HashMap<>();
        messageAttributesAdd.put("MessageType", MessageAttributeValue.builder()
                .stringValue("addWorkload").dataType("String").build());
        messageAttributesDelete.put("MessageType", MessageAttributeValue.builder()
                .stringValue("deleteWorkload").dataType("String").build());
        messageAdd = Message.builder().body("Message").messageAttributes(messageAttributesAdd).build();
        messageDelete = Message.builder().body("Message").messageAttributes(messageAttributesDelete).build();
        messageResponseAdd = ReceiveMessageResponse.builder().messages(List.of(messageAdd)).build();
        messageResponseDelete = ReceiveMessageResponse.builder().messages(List.of(messageDelete)).build();
    }

    @Test
    void testReceiveMessage() throws JsonProcessingException {
        ReportTrainerWorkloadResponse response = new ReportTrainerWorkloadResponse();
        when(sqsClient.receiveMessage(addRequest))
                .thenReturn(messageResponseAdd);
        when(objectMapper.readValue("Message", TrainerWorkloadRequest.class))
                .thenReturn(request);
        when(workloadService.addWorkload(request)).thenReturn(response);
        TrainerWorkloadResponse result = sqsWorkloadReceiver.receiveMessage();
        assertEquals(response, result);
        verify(workloadService, times(1)).addWorkload(request);
    }


    @Test
    void testReceiveDeleteMessage() throws JsonProcessingException {
        when(sqsClient.receiveMessage(addRequest))
                .thenReturn(messageResponseDelete);
        when(objectMapper.readValue("Message", TrainerWorkloadRequest.class))
                .thenReturn(request);
        TrainerWorkloadResponse result = sqsWorkloadReceiver.receiveMessage();
        verify(workloadService, times(1)).deleteWorkload(request);
    }
}