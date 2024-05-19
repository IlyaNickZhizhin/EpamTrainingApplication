package org.epam.reportservice.service;

import org.epam.common.dto.TrainerWorkloadRequest;
import org.epam.reportservice.Reader;
import org.epam.reportservice.dto.ReportTrainerWorkloadRequest;
import org.epam.reportservice.dto.ReportTrainerWorkloadResponse;
import org.epam.reportservice.mapper.MongoToDynamoMapper;
import org.epam.reportservice.model.TrainingSession;
import org.epam.reportservice.model.Workload;
import org.epam.reportservice.model.WorkloadDynamo;
import org.epam.reportservice.repository.WorkloadRepositoryDynamo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkloadCloudServiceTest {

    @Mock
    private WorkloadRepositoryDynamo repository;

    @Spy
    private MongoToDynamoMapper mapper= Mappers.getMapper(MongoToDynamoMapper.class);

    @InjectMocks
    private WorkloadCloudService service;

    private final Reader reader =  new Reader();
    private TrainerWorkloadRequest request;
    private WorkloadDynamo workload;


    @BeforeEach
    void setUp() {
        request = reader
                .readEntity("src/test/resources/models/workloads/workload1.json", ReportTrainerWorkloadRequest.class);
        workload = mapper.mongoToDynamo(Workload.of(request));
    }

    @Test
    void addWorkload() {
        when(repository.findById(anyString(), anyString())).thenReturn(Optional.empty());
        when(repository.save(any(WorkloadDynamo.class))).thenReturn(workload);
        assertEquals(ReportTrainerWorkloadResponse.of(mapper.dynamoToMongo(workload)), service.addWorkload(request));
        verify(repository, times(1)).findById(anyString(), anyString());
        verify(repository, times(1)).save(any(WorkloadDynamo.class));
    }


    @Test
    void deleteWorkload() {
        workload.setTrainingSessions(new TreeSet<>(Arrays.asList(TrainingSession.of(request.getTrainingDate(), request.getDuration()))));
        when(repository.findById(anyString(), anyString())).thenReturn(Optional.of(workload));
        when(repository.save(any(WorkloadDynamo.class))).thenReturn(workload);
        assertEquals(ReportTrainerWorkloadResponse.of(mapper.dynamoToMongo(workload)), service.deleteWorkload(request));
        verify(repository, times(1)).findById(anyString(), anyString());
        verify(repository, times(1)).save(any(WorkloadDynamo.class));
    }

    @Test
    void deleteWorkloadException() {
        workload.setTrainingSessions(new TreeSet<>(Arrays.asList(TrainingSession.of(request.getTrainingDate(), request.getDuration()))));
        when(repository.findById(request.getUsername(), String.valueOf(request.isActive()))).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.deleteWorkload(request));
    }

    @Test
    void deleteWorkloadExceptionTwo() {
        workload.setTrainingSessions(new TreeSet<>(Arrays.asList(TrainingSession.of(request.getTrainingDate(), request.getDuration()))));
        when(repository.findById(request.getUsername(), String.valueOf(request.isActive()))).thenReturn(Optional.of(workload));
        TrainerWorkloadRequest request2 = reader
                .readEntity("src/test/resources/models/workloads/workload2.json", ReportTrainerWorkloadRequest.class);
        assertThrows(NoSuchElementException.class, () -> service.deleteWorkload(request2));
    }
}
