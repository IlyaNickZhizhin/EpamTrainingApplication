package org.epam.dao.gymDao;

import org.epam.model.gymModel.Trainer;
import org.epam.storageInFile.Storage;
import org.epam.model.gymModel.Training;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainingDaoTest {

    static TrainingDao trainingDao;

    @BeforeAll
    public static void beforeAll() {
        Storage mockStorage = mock(Storage.class);
        HashMap<Integer, Training> trainings = new HashMap<>();
        HashMap<String, HashMap<Integer, Training>> models = new HashMap<>();
        models.put(Training.class.getName(), trainings);
        when(mockStorage.getGymModels()).thenReturn(models);
        trainingDao = new TrainingDao(mockStorage);
    }

    @Test
    public void testCreateTraining() {
        Training training = new Training();
        trainingDao.create(training);
        assertEquals(training, trainingDao.get(1));
    }

    @Test
    public void testSaveTraining() {
        Training training = new Training();
        training.setId(1);
        Training training2 = new Training();
        training2.setId(2);
        training2.setTrainingName("Test training");
        trainingDao.save(training);
        trainingDao.save(training2);
        assertEquals(training, trainingDao.get(training.getId()));
        assertNotEquals(training2, trainingDao.get(training.getId()));
    }

    @Test
    public void testUpdateTraining() {
        Training training = new Training();
        training.setId(1);
        trainingDao.save(training);
        Training updatedTraining = new Training();
        updatedTraining.setId(1);
        updatedTraining.setTrainingName("Test training");
        trainingDao.update(training.getId(), updatedTraining);
        assertEquals(updatedTraining, trainingDao.get(training.getId()));
    }

    @Test
    public void testDeleteTraining() {
        Training training = new Training();
        trainingDao.save(training);
        trainingDao.delete(training.getId());
        assertNull(trainingDao.get(training.getId()));
    }

    @Test
    public void testGetTraining() {
        Training training = new Training();
        trainingDao.save(training);
        assertEquals(training, trainingDao.get(training.getId()));
    }
}
