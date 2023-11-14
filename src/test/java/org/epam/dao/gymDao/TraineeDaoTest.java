package org.epam.dao.gymDao;

import org.epam.config.Storage;
import org.epam.model.gymModel.Trainee;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
class TraineeDaoTest {

    static TraineeDao traineeDao;
    @BeforeAll
    public static void beforeAll() {
        Storage mockStorage = mock(Storage.class);
        HashMap<Integer, Trainee> trainees = new HashMap<>();
        HashMap<String, HashMap<Integer, Trainee>> models = new HashMap<>();
        models.put("trainees", trainees);
        when(mockStorage.getGymModels()).thenReturn(models);
        traineeDao = new TraineeDao(mockStorage);
    }

    @Test
    public void testCreateTrainee() {
        Trainee trainee = new Trainee();
        traineeDao.create(trainee);
        assertEquals(trainee, traineeDao.get(1));
    }

    @Test
    public void testSaveTrainee() {
        Trainee trainee = new Trainee();
        trainee.setId(1);
        Trainee trainee2 = new Trainee();
        trainee2.setId(2);
        trainee2.setAddress("Test address");
        traineeDao.save(trainee);
        traineeDao.save(trainee2);
        assertEquals(trainee, traineeDao.get(trainee.getId()));
        assertNotEquals(trainee2, traineeDao.get(trainee.getId()));
    }

    @Test
    public void testUpdateTrainee() {
        Trainee trainee = new Trainee();
        trainee.setId(1);
        traineeDao.save(trainee);
        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setId(1);
        updatedTrainee.setAddress("Test address");
        traineeDao.update(trainee.getId(), updatedTrainee);
        assertEquals(updatedTrainee, traineeDao.get(trainee.getId()));
    }

    @Test
    public void testDeleteTrainee() {
        Trainee trainee = new Trainee();
        traineeDao.save(trainee);
        traineeDao.delete(trainee.getId());
        assertNull(traineeDao.get(trainee.getId()));
    }

    @Test
    public void testGetTrainee() {
        Trainee trainee = new Trainee();
        traineeDao.save(trainee);
        assertEquals(trainee, traineeDao.get(trainee.getId()));
    }

}