package org.epam.mainservice.dto;

import org.epam.mainservice.Reader;
import org.epam.gymservice.dto.reportDto.TrainerWorkloadRequest;
import org.epam.gymservice.dto.reportDto.TrainerWorkloadResponse;
import org.epam.gymservice.mapper.TrainingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TrainerWorkloadResponseTest {

    TrainingMapper mapper = Mappers.getMapper(TrainingMapper.class);
    TrainerWorkloadResponse response1;
    TrainerWorkloadResponse response2;
    TrainerWorkloadRequest request;

    @BeforeEach
    void setUp(){
        Reader reader = new Reader();
        reader.setStartPath("");
        reader.setEndPath("");
        request = reader
                .readEntity("src/test/resources/models/workloads/workload1.json", TrainerWorkloadRequest.class);
        response1 = mapper.trainerWorkloadRequestToResponse(reader
                .readEntity("src/test/resources/models/workloads/workload1.json", TrainerWorkloadRequest.class));
        response2 = mapper.trainerWorkloadRequestToResponse(reader
                .readEntity("src/test/resources/models/workloads/workload1.json", TrainerWorkloadRequest.class));

    }
    @Test
    void testEqualsTrue() {
        assertEquals(response1, response2);
    }

    @Test
    void testEqualsFalse() {
        Map<Month, Double> resultLoad = new HashMap<>();
        resultLoad.put(request.getTrainingDate().getMonth(), request.getDuration());
        Map<Year, Map<Month, Double>> resultYear = new HashMap<>();
        resultYear.put(Year.of(request.getTrainingDate().getYear()), resultLoad);
        response2.setMap(resultYear);
        assertNotEquals(response1, response2);
    }

    @Test
    void testHashCode() {
        assertEquals(response1.hashCode(), response2.hashCode());
        Map<Month, Double> resultLoad = new HashMap<>();
        resultLoad.put(request.getTrainingDate().getMonth(), request.getDuration());
        Map<Year, Map<Month, Double>> resultYear = new HashMap<>();
        resultYear.put(Year.of(request.getTrainingDate().getYear()), resultLoad);
        response2.setMap(resultYear);
        assertNotEquals(response1.hashCode(), response2.hashCode());
    }
}