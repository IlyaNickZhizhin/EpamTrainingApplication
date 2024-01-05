package org.epam.mainservice.dto;

import org.epam.mainservice.Reader;
import org.epam.gymservice.dto.traineeDto.TraineeDto;
import org.epam.gymservice.mapper.TraineeMapper;
import org.epam.gymservice.model.gymModel.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TraineeDtoTest {

    TraineeDto dto1;
    TraineeDto dto2;

    TraineeMapper mapper = Mappers.getMapper(TraineeMapper.class);

    @BeforeEach
    void setUp(){
        Reader reader = new Reader();
        reader.setEndPath("");
        reader.setStartPath("");
        dto1 = reader.readDto("src/test/resources/models/trainees/trainee3.json", Trainee.class, mapper::traineeToShortTraineeDto);
        dto2 = reader.readDto("src/test/resources/models/trainees/trainee3.json", Trainee.class, mapper::traineeToShortTraineeDto);
    }

    @Test
    void testEqualsTrue() {
        assertEquals(dto1, dto2);
    }
    @Test
    void testEqualsFalse() {
        dto2.setFirstname(dto1.getFirstname()+"2");
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testHashCode() {
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setFirstname(dto1.getFirstname()+"2");
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }
}