package org.epam.mapper;

import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.storageInFile.Storage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class FileToModelsMapperTest {

    private static final Storage storage = new Storage();

    private static final FileToModelsMapper mapper = new FileToModelsMapper<>(storage, "src/main/resources/initFile.txt");

    @BeforeAll
    public static void setUp() throws IOException {
        storage.init();
        mapper.init();
    }
    @Test
    public void testInitUsers() {
        mapper.initUsers();
        assertFalse(storage.getUsers().isEmpty());
    }

    @Test
    public void testInitModels() {
        mapper.initModels(Trainer.class);
        mapper.initModels(Trainee.class);
        mapper.initModels(Training.class);
        assertFalse(storage.getModels(Trainer.class.getName()).isEmpty());
        assertFalse(storage.getModels(Trainee.class.getName()).isEmpty());
        assertFalse(storage.getModels(Training.class.getName()).isEmpty());
    }

    @Test
    public void testWriteModels() {
        assertDoesNotThrow(() -> mapper.writeModels());
    }
}