package org.epam.storageInFile;

import org.epam.testBeans.filereader.FileToModelsMapper;
import org.epam.model.gymModel.Trainer;
import org.epam.testBeans.storageInFile.DataInitializer;
import org.epam.testBeans.storageInFile.DataSaver;
import org.epam.testBeans.storageInFile.Storage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DataInitializerSaverTest {

    private static final Storage storage = new Storage();
    private static DataInitializer dataInitializer;
    private static FileToModelsMapper mapper;
    private static DataSaver dataSaver;

    @BeforeAll
    public static void setup() throws IOException {
        storage.init();
        mapper = new FileToModelsMapper<>(storage, "src/main/resources/initFile.txt");
        mapper.init();
        dataInitializer = new DataInitializer(mapper);
        dataSaver = new DataSaver(mapper);
    }

    @Test
    public void testInit() {
        assertDoesNotThrow(() -> dataInitializer.init());
        assertFalse(storage.getModels(Trainer.class.getName()).isEmpty());
//        assertFalse(storage.getModels(Trainee.class.getName()).isEmpty());
//        assertFalse(storage.getModels(Training.class.getName()).isEmpty());
    }
    @Test
    public void testSaveData() throws IOException {
        assertDoesNotThrow(() -> dataSaver.saveData());
    }
}
