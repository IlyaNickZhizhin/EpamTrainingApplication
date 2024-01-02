package org.epam.reportservice.dto;

import org.epam.reportservice.Reader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TrainerWorkloadRequestTest {

    TrainerWorkloadRequest request1;
    TrainerWorkloadRequest request2;

    @BeforeEach
    void setUp(){
        Reader reader = new Reader();
        request1 = reader
                .readEntity("src/test/resources/models/workloads/workload1.json", TrainerWorkloadRequest.class);
        request2 = reader
                .readEntity("src/test/resources/models/workloads/workload1.json", TrainerWorkloadRequest.class);

    }

    @Test
    void testEqualsTrue() {
        assertEquals(request1, request2);
    }
    @Test
    void testEqualsFalse() {
        request2.setFirstName(request1.getFirstName()+"2");
        assertNotEquals(request1, request2);
    }

    @Test
    void testHashCode() {
        assertEquals(request1.hashCode(), request2.hashCode());
        request2.setFirstName(request1.getFirstName()+"2");
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }
}