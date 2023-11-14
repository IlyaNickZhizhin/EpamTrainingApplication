package org.epam.dao.gymDao;

import org.epam.config.Storage;
import org.epam.model.gymModel.Trainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainerDaoTest {

    static TrainerDao trainerDao;

    @BeforeAll
    public static void beforeAll() {
        Storage mockStorage = mock(Storage.class);
        HashMap<Integer, Trainer> trainers = new HashMap<>();
        HashMap<String, HashMap<Integer, Trainer>> models = new HashMap<>();
        models.put("trainers", trainers);
        when(mockStorage.getGymModels()).thenReturn(models);
        trainerDao = new TrainerDao(mockStorage);
    }

    @Test
    public void testCreateTrainer() {
        Trainer trainer = new Trainer();
        trainerDao.create(trainer);
        assertEquals(trainer, trainerDao.get(1));
    }

    @Test
    public void testSaveTrainer() {
        Trainer trainer = new Trainer();
        trainer.setId(1);
        Trainer trainer2 = new Trainer();
        trainer2.setId(2);
        trainer2.setSpecialization("Test specialization");
        trainerDao.save(trainer);
        trainerDao.save(trainer2);
        assertEquals(trainer, trainerDao.get(trainer.getId()));
        assertNotEquals(trainer2, trainerDao.get(trainer.getId()));
    }

    @Test
    public void testUpdateTrainer() {
        Trainer trainer = new Trainer();
        trainer.setId(1);
        trainerDao.save(trainer);
        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setId(1);
        updatedTrainer.setSpecialization("Test specialization");
        trainerDao.update(trainer.getId(), updatedTrainer);
        assertEquals(updatedTrainer, trainerDao.get(trainer.getId()));
    }

    @Test
    public void testDeleteTrainer() {
        Trainer trainer = new Trainer();
        trainerDao.save(trainer);
        trainerDao.delete(trainer.getId());
        assertNull(trainerDao.get(trainer.getId()));
    }

    @Test
    public void testGetTrainer() {
        Trainer trainer = new Trainer();
        trainerDao.save(trainer);
        assertEquals(trainer, trainerDao.get(trainer.getId()));
    }
}
