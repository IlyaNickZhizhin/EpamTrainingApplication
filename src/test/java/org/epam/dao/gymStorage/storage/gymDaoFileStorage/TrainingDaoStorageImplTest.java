package org.epam.dao.gymStorage.storage.gymDaoFileStorage;

import org.epam.model.gymModel.Training;
import org.epam.testBeans.dao.gymDaoFileStorage.TrainingDaoStorageImpl;
import org.epam.testBeans.storageInFile.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class TrainingDaoStorageImplTest {

    Storage mockStorage = mock(Storage.class);
    @InjectMocks
    TrainingDaoStorageImpl trainingDaoStorageImpl;

    @BeforeEach
    public void beforeEach() {
        initMocks(this);
        HashMap<Integer, Training> trainings = new HashMap<>();
        HashMap<String, HashMap<Integer, Training>> models = new HashMap<>();
        models.put(Training.class.getName(), trainings);
        when(mockStorage.getGymModels()).thenReturn(models);
    }

    @Test
    public void testCreateTraining() {
        Training training = new Training();
        trainingDaoStorageImpl.create(training);
        assertEquals(training, trainingDaoStorageImpl.get(0));
    }

    @Test
    public void testSaveTraining() {
        Training training = new Training();
        training.setId(1);
        Training training2 = new Training();
        training2.setId(2);
        training2.setTrainingName("Test training");
        trainingDaoStorageImpl.save(training);
        trainingDaoStorageImpl.save(training2);
        assertEquals(training, trainingDaoStorageImpl.get(training.getId()));
        assertNotEquals(training2, trainingDaoStorageImpl.get(training.getId()));
    }

    @Test
    public void testUpdateTraining() {
        Training training = new Training();
        training.setId(1);
        trainingDaoStorageImpl.save(training);
        Training updatedTraining = new Training();
        updatedTraining.setId(1);
        updatedTraining.setTrainingName("Test training");
        trainingDaoStorageImpl.update(training.getId(), updatedTraining);
        assertEquals(updatedTraining, trainingDaoStorageImpl.get(training.getId()));
    }

    @Test
    public void testDeleteTraining() {
        Training training = new Training();
        trainingDaoStorageImpl.save(training);
        trainingDaoStorageImpl.delete(training.getId());
        assertNull(trainingDaoStorageImpl.get(training.getId()));
    }

    @Test
    public void testGetTraining() {
        Training training = new Training();
        trainingDaoStorageImpl.save(training);
        assertEquals(training, trainingDaoStorageImpl.get(training.getId()));
    }
}
