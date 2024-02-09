package org.epam.reportservice.service;

import org.epam.common.dto.TrainerWorkloadRequest;
import org.epam.reportservice.Reader;
import org.epam.reportservice.dto.ReportTrainerWorkloadRequest;
import org.epam.reportservice.dto.ReportTrainerWorkloadResponse;
import org.epam.reportservice.model.TrainingSession;
import org.epam.reportservice.model.Workload;
import org.epam.reportservice.repository.WorkloadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkloadServiceTest {

    @Mock
    private WorkloadRepository repository;

    @InjectMocks
    private WorkloadService service;

    private final Reader reader =  new Reader();
    private TrainerWorkloadRequest request;
    private Workload workload;


    @BeforeEach
    void setUp() {
        request = reader
                .readEntity("src/test/resources/models/workloads/workload1.json", ReportTrainerWorkloadRequest.class);
        workload = Workload.of(request);
    }

    @Test
    void addWorkload() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Workload.class))).thenReturn(workload);
        assertEquals(ReportTrainerWorkloadResponse.of(workload), service.addWorkload(request));
        verify(repository, times(1)).findById(anyString());
        verify(repository, times(1)).save(any(Workload.class));
    }

    @Test
    void deleteWorkload() {
        workload.setTrainingSessions(new TreeSet<>(Arrays.asList(TrainingSession.of(request.getTrainingDate(), request.getDuration()))));
        when(repository.findById(anyString())).thenReturn(Optional.of(workload));
        when(repository.save(any(Workload.class))).thenReturn(workload);
        assertEquals(ReportTrainerWorkloadResponse.of(workload), service.deleteWorkload(request));
        verify(repository, times(1)).findById(anyString());
        verify(repository, times(1)).save(any(Workload.class));
    }
}