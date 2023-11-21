package org.epam.mapper;

import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.epam.testBeans.filereader.FileToModelsMapper;
import org.epam.testBeans.storageInFile.Storage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FileToModelsMapperTest {

    private static final Storage storage = new Storage();

    private static final FileToModelsMapper mapper = new FileToModelsMapper<>(storage, "src/main/resources/initFile.json");

    @BeforeAll
    public static void setUp() throws IOException {
        storage.init();
        mapper.init();
    }
    @Test
    public void testInit() {
        mapper.initUsers();
        mapper.initTrainingTypes();
        mapper.initTrainers();
        mapper.initTrainees();
        mapper.initTrainings();
        assertFalse(storage.getUsers().isEmpty());
        assertFalse(storage.getModels(TrainingType.class.getName()).isEmpty());
        assertFalse(storage.getModels(Trainer.class.getName()).isEmpty());
        assertFalse(storage.getModels(Trainee.class.getName()).isEmpty());
        assertFalse(storage.getModels(Training.class.getName()).isEmpty());
    }

}