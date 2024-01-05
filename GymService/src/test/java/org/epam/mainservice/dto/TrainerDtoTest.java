package org.epam.mainservice.dto;

import org.epam.mainservice.Reader;
import org.epam.gymservice.dto.trainerDto.TrainerDto;
import org.epam.gymservice.mapper.TrainerMapper;
import org.epam.gymservice.model.gymModel.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TrainerDtoTest {

    TrainerDto dto1;
    TrainerDto dto2;

    TrainerMapper mapper = Mappers.getMapper(TrainerMapper.class);

    @BeforeEach
    void setUp(){
        Reader reader = new Reader();
        reader.setEndPath("");
        reader.setStartPath("");
        dto1 = reader.readDto("src/test/resources/models/trainers/trainer1.json", Trainer.class, mapper::trainerToTrainerDto);
        dto2 = reader.readDto("src/test/resources/models/trainers/trainer1.json", Trainer.class, mapper::trainerToTrainerDto);
    }

    @Test
    void testEqualsTrue() {
        assertEquals(dto1, dto2);
    }
    @Test
    void testEqualsFalse() {
        dto2.setFirstName(dto1.getFirstName()+"2");
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testHashCode() {
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setFirstName(dto1.getFirstName()+"2");
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }
}