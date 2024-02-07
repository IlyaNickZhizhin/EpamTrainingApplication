package org.epam.reportservice.service;

import org.epam.common.dto.TrainingSession;
import org.epam.reportservice.Reader;
import org.epam.reportservice.dto.ReportTrainerWorkloadRequest;
import org.epam.reportservice.dto.ReportTrainerWorkloadResponse;
import org.epam.reportservice.model.TrainerKey;
import org.epam.reportservice.repository.WorkloadStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.PriorityQueue;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkloadServiceTest {
    @Mock
    WorkloadStorage storage;
    @InjectMocks
    WorkloadService service;

    @Test
    void testChangeAdd() {
        Reader reader = new Reader();
        ReportTrainerWorkloadRequest request = reader
                .readEntity("src/test/resources/models/workloads/workload1.json", ReportTrainerWorkloadRequest.class);
        TrainerKey key = TrainerKey.of(request);
        TrainingSession newSession = TrainingSession.of(request);
        Queue<TrainingSession> queue = new PriorityQueue<>();
        queue.add(newSession);
        when(storage.addWorkload(key, newSession)).thenReturn(queue);
        ReportTrainerWorkloadResponse response = ReportTrainerWorkloadResponse.of(key, queue);
        assertEquals(response, service.addWorkload(request));
    }
    @Test
    void testChangeDelete() {
        Reader reader = new Reader();
        ReportTrainerWorkloadRequest request = reader
                .readEntity("src/test/resources/models/workloads/workload1.json", ReportTrainerWorkloadRequest.class);
        TrainerKey key = TrainerKey.of(request);
        TrainingSession newSession = TrainingSession.of(request);
        Queue<TrainingSession> queue = new PriorityQueue<>();
        queue.add(newSession);
        Queue<TrainingSession> queue2 = new PriorityQueue<>(queue);
        queue2.remove(newSession);
        when(storage.deleteWorkload(key, newSession)).thenReturn(queue2);
        ReportTrainerWorkloadResponse response = ReportTrainerWorkloadResponse.of(key, queue2);
        assertEquals(response, service.deleteWorkload(request));
    }

}