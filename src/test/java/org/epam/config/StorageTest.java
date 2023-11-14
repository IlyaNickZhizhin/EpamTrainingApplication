package org.epam.config;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class StorageTest {

    private final Storage storage = new Storage();

    @Test
    public void testGetTrainers() {
        assertNotNull(storage.getModels("trainers"));
    }

    @Test
    public void testGetTrainees() {
        assertNotNull(storage.getModels("trainees"));
    }

    @Test
    public void testGetTrainings() {
        assertNotNull(storage.getModels("trainings"));
    }

    @Test
    public void testInit() {
        File tempFile;
        try {
            tempFile = File.createTempFile("tempFile", ".txt");
            String initFileContent = new String(Files.readAllBytes(Paths.get("src/main/resources/initFile.txt")));
            Files.write(Paths.get(tempFile.getAbsolutePath()), initFileContent.getBytes());
            storage.setInitFile(tempFile.getAbsolutePath());
            assertDoesNotThrow(() -> storage.init());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveData() {
        File tempFile;
        try {
            tempFile = File.createTempFile("tempFile", ".txt");
            storage.setInitFile(tempFile.getAbsolutePath());
            assertDoesNotThrow(() -> storage.saveData());
            String savedData = new String(Files.readAllBytes(Paths.get(tempFile.getAbsolutePath())));
            assertFalse(savedData.isEmpty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

