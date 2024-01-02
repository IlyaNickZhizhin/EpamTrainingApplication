package org.epam.reportservice.controller;

import org.epam.reportservice.Reader;
import org.epam.reportservice.dto.TrainerWorkloadRequest;
import org.epam.reportservice.dto.TrainerWorkloadResponse;
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
    void testChange() {
        Reader reader = new Reader();
        TrainerWorkloadRequest request = reader
                .readEntity("src/test/resources/models/workloads/workload1.json", TrainerWorkloadRequest.class);
        when(service.change(request)).thenReturn(new TrainerWorkloadResponse());
        assertEquals(new ResponseEntity<>(new TrainerWorkloadResponse(), HttpStatus.OK),
                controller.change(request));
    }
}