package org.epam.reportservice.dto;

import org.epam.reportservice.Reader;
import org.epam.reportservice.mapper.ReportMapper;
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

    ReportMapper mapper = Mappers.getMapper(ReportMapper.class);
    TrainerWorkloadResponse response1;
    TrainerWorkloadResponse response2;
    TrainerWorkloadRequest request;

    @BeforeEach
    void setUp(){
        Reader reader = new Reader();
        request = reader
                .readEntity("src/test/resources/models/workloads/workload1.json", TrainerWorkloadRequest.class);
        response1 = mapper.RequestToResponse(reader
                .readEntity("src/test/resources/models/workloads/workload1.json", TrainerWorkloadRequest.class));
        response2 = mapper.RequestToResponse(reader
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