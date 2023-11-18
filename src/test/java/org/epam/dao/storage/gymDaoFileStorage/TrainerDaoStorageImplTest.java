package org.epam.dao.storage.gymDaoFileStorage;

import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.TrainingType;
import org.epam.storageInFile.Storage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainerDaoStorageImplTest {

    static TrainerDaoStorageImpl trainerDaoStorageImpl;

    @BeforeAll
    public static void beforeAll() {
        Storage mockStorage = mock(Storage.class);
        HashMap<Integer, Trainer> trainers = new HashMap<>();
        HashMap<String, HashMap<Integer, Trainer>> models = new HashMap<>();
        models.put(Trainer.class.getName(), trainers);
        when(mockStorage.getGymModels()).thenReturn(models);
        trainerDaoStorageImpl = new TrainerDaoStorageImpl(mockStorage);
    }

    @Test
    public void testCreateTrainer() {
        Trainer trainer = new Trainer();
        trainerDaoStorageImpl.create(trainer);
        assertEquals(trainer, trainerDaoStorageImpl.get(1));
    }

    @Test
    public void testSaveTrainer() {
        Trainer trainer = new Trainer();
        trainer.setId(1);
        Trainer trainer2 = new Trainer();
        trainer2.setId(2);
        trainer2.setSpecialization(new TrainingType());
        trainerDaoStorageImpl.save(trainer);
        trainerDaoStorageImpl.save(trainer2);
        assertEquals(trainer, trainerDaoStorageImpl.get(trainer.getId()));
        assertNotEquals(trainer2, trainerDaoStorageImpl.get(trainer.getId()));
    }

    @Test
    public void testUpdateTrainer() {
        Trainer trainer = new Trainer();
        trainer.setId(1);
        trainerDaoStorageImpl.save(trainer);
        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setId(1);
        updatedTrainer.setSpecialization(new TrainingType());
        trainerDaoStorageImpl.update(trainer.getId(), updatedTrainer);
        assertEquals(updatedTrainer, trainerDaoStorageImpl.get(trainer.getId()));
    }

    @Test
    public void testDeleteTrainer() {
        Trainer trainer = new Trainer();
        trainerDaoStorageImpl.save(trainer);
        trainerDaoStorageImpl.delete(trainer.getId());
        assertNull(trainerDaoStorageImpl.get(trainer.getId()));
    }

    @Test
    public void testGetTrainer() {
        Trainer trainer = new Trainer();
        trainerDaoStorageImpl.save(trainer);
        assertEquals(trainer, trainerDaoStorageImpl.get(trainer.getId()));
    }
}
