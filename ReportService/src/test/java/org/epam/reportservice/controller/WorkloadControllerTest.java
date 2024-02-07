package org.epam.reportservice.controller;

import org.epam.reportservice.Reader;
import org.epam.reportservice.dto.ReportTrainerWorkloadRequest;
import org.epam.reportservice.dto.ReportTrainerWorkloadResponse;
import org.epam.reportservice.service.WorkloadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkloadControllerTest {

    @Mock
    private WorkloadService service;
    @InjectMocks
    private WorkloadController controller;
    @Test
    void testAdd() {
        Reader reader = new Reader();
        ReportTrainerWorkloadRequest request = reader
                .readEntity("src/test/resources/models/workloads/workload1.json", ReportTrainerWorkloadRequest.class);
        when(service.addWorkload(request)).thenReturn(new ReportTrainerWorkloadResponse());
        assertEquals(new ResponseEntity<>(new ReportTrainerWorkloadResponse(), HttpStatus.OK),
                controller.add(request));
    }
    @Test
    void testDelete() {
        Reader reader = new Reader();
        ReportTrainerWorkloadRequest request = reader
                .readEntity("src/test/resources/models/workloads/workload1.json", ReportTrainerWorkloadRequest.class);
        when(service.deleteWorkload(request)).thenReturn(new ReportTrainerWorkloadResponse());
        assertEquals(new ResponseEntity<>(new ReportTrainerWorkloadResponse(), HttpStatus.OK),
                controller.delete(request));
    }
}