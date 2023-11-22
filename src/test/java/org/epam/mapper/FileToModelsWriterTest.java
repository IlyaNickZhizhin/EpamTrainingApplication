package org.epam.mapper;

import org.epam.testBeans.filereader.FileToModelsWriter;
import org.epam.testBeans.storageInFile.Storage;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class FileToModelsWriterTest {

    private static final Storage storage = new Storage();

    private static final FileToModelsWriter WRITER = new FileToModelsWriter(storage, "src/test/resources/initFile.json");

    @Test
    public void testInit() throws IOException {
        WRITER.init();
        WRITER.initUsers();
        WRITER.initTrainingTypes();
        WRITER.initTrainers();
        WRITER.initTrainees();
        WRITER.initTrainings();
        assertFalse(storage.users.isEmpty());
        assertFalse(storage.trainingTypes.isEmpty());
        assertFalse(storage.trainers.isEmpty());
        assertFalse(storage.trainees.isEmpty());
        assertFalse(storage.trainings.isEmpty());
    }

}