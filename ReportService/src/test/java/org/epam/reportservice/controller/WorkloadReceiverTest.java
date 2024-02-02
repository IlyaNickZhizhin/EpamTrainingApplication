package org.epam.reportservice.controller;

import org.epam.common.dto.TrainerWorkloadResponse;
import org.epam.reportservice.Reader;
import org.epam.reportservice.dto.ReportTrainerWorkloadRequest;
import org.epam.reportservice.dto.ReportTrainerWorkloadResponse;
import org.epam.reportservice.service.WorkloadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkloadReceiverTest {
    @Mock
    private WorkloadService workloadService;
    @Mock
    private JmsTemplate jmsTemplate;
    @InjectMocks
    private WorkloadReceiver workloadReceiver;

    Reader reader = new Reader();
    ReportTrainerWorkloadRequest request;

    @BeforeEach
    void setUp() {
        request = reader
                .readEntity("src/test/resources/models/workloads/workload1.json", ReportTrainerWorkloadRequest.class);
    }


    @Test
    void testReceiveAddMessage() {
        ReportTrainerWorkloadResponse response = new ReportTrainerWorkloadResponse();
        when(workloadService.addWorkload(request)).thenReturn(response);
        TrainerWorkloadResponse result = workloadReceiver.receiveAddMessage(request);
        assertEquals(response, result);
        verify(workloadService, times(1)).addWorkload(request);
    }

    @Test
    void testReceiveAddMessageException() {
        when(workloadService.addWorkload(request)).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> workloadReceiver.receiveAddMessage(request));
        verify(jmsTemplate, times(1)).convertAndSend("DLQ.addTrainingRequestQueue", request);
    }

    @Test
    void testReceiveDeleteMessage() {
        ReportTrainerWorkloadResponse response = new ReportTrainerWorkloadResponse();
        when(workloadService.deleteWorkload(request)).thenReturn(response);
        TrainerWorkloadResponse result = workloadReceiver.receiveDeleteMessage(request);
        assertEquals(response, result);
        verify(workloadService, times(1)).deleteWorkload(request);
    }

    @Test
    void testReceiveDeleteMessageException() {
        when(workloadService.deleteWorkload(request)).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> workloadReceiver.receiveDeleteMessage(request));
        verify(jmsTemplate, times(1)).convertAndSend("DLQ.deleteTrainingRequestQueue", request);
    }
}
