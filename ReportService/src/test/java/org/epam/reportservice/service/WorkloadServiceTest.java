package org.epam.reportservice.service;

import org.epam.reportservice.Reader;
import org.epam.reportservice.dto.TrainerWorkloadRequest;
import org.epam.reportservice.dto.TrainerWorkloadResponse;
import org.epam.reportservice.mapper.ReportMapper;
import org.epam.reportservice.repository.WorkloadStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkloadServiceTest {
    @Mock
    WorkloadStorage storage;
    @Spy
    ReportMapper mapper = Mappers.getMapper(ReportMapper.class);
    @InjectMocks
    WorkloadService service;

    @Test
    void testChange() {
        Reader reader = new Reader();
        TrainerWorkloadRequest request = reader
                .readEntity("src/test/resources/models/workloads/workload1.json", TrainerWorkloadRequest.class);
        Map<String, Map<Year, Map<Month, Double>>> map = new HashMap<>();
        when(storage.getStorage()).thenReturn(map);
        Map<Month, Double> resultLoad = new HashMap<>();
        resultLoad.put(request.getTrainingDate().getMonth(), request.getDuration());
        Map<Year, Map<Month, Double>> resultYear = new HashMap<>();
        resultYear.put(Year.of(request.getTrainingDate().getYear()), resultLoad);
        TrainerWorkloadResponse response = mapper.RequestToResponse(request);
        response.setMap(resultYear);
        assertEquals(response, service.change(request));
    }
}