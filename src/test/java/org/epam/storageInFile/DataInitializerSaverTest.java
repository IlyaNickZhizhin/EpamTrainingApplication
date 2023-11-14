package org.epam.storageInFile;

import org.epam.mapper.FileToModelsMapper;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

public class DataInitializerSaverTest {

    private static Storage storage = new Storage();;
    private static DataInitializer dataInitializer;
    private static FileToModelsMapper mapper;
    private static DataSaver dataSaver;

    @BeforeAll
    public static void setup() throws IOException {
        storage.init();
        mapper = new FileToModelsMapper<>(storage, "src/main/resources/initFile.txt");
        mapper.init();
        dataInitializer = new DataInitializer(mapper);
        dataSaver = new DataSaver(storage, mapper);
    }

    @Test
    public void testInit() {
        assertDoesNotThrow(() -> dataInitializer.init());
        assertFalse(storage.getModels(Trainer.class.getName()).isEmpty());
        assertFalse(storage.getModels(Trainee.class.getName()).isEmpty());
        assertFalse(storage.getModels(Training.class.getName()).isEmpty());
    }
    @Test
    public void testSaveData() throws IOException {
        assertDoesNotThrow(() -> dataSaver.saveData());
    }
}
