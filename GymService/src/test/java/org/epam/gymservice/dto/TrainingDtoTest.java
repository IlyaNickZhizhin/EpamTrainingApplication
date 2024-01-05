package org.epam.gymservice.dto;

import org.epam.gymservice.Reader;
import org.epam.gymservice.dto.trainingDto.TrainingDto;
import org.epam.gymservice.mapper.TrainerMapper;
import org.epam.gymservice.mapper.TrainingMapper;
import org.epam.gymservice.model.gymModel.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class TrainingDtoTest {


    TrainingDto dto1;
    TrainingDto dto2;
    @Spy
    TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);
    @Spy
    @InjectMocks
    TrainingMapper mapper = Mappers.getMapper(TrainingMapper.class);

    @BeforeEach
    void setUp(){
        Reader reader = new Reader();
        reader.setEndPath("");
        reader.setStartPath("");
        dto1 = reader.readDto("src/test/resources/models/trainings/training1.json", Training.class, mapper::trainerTrainingToShortDto);
        dto2 = reader.readDto("src/test/resources/models/trainings/training1.json", Training.class, mapper::trainerTrainingToShortDto);
    }

    @Test
    void testEqualsTrue() {
        assertEquals(dto1, dto2);
    }
    @Test
    void testEqualsFalse() {
        dto2.setTrainingName(dto1.getTrainingName()+"2");
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testHashCode() {
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTrainingName(dto1.getTrainingName()+"2");
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }
}