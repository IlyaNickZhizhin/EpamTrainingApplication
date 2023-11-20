package org.epam.dao.gymStorage.storage.gymDaoFileStorage;

import org.epam.dao.gymStorage.gymDaoFileStorage.TraineeDaoStorageImpl;
import org.epam.model.gymModel.Trainee;
import org.epam.storageInFile.Storage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
class TraineeDaoStorageImplTest {

    static TraineeDaoStorageImpl traineeDaoStorageImpl;
    @BeforeAll
    public static void beforeAll() {
        Storage mockStorage = mock(Storage.class);
        HashMap<Integer, Trainee> trainees = new HashMap<>();
        HashMap<String, HashMap<Integer, Trainee>> models = new HashMap<>();
        models.put(Trainee.class.getName(), trainees);
        when(mockStorage.getGymModels()).thenReturn(models);
        traineeDaoStorageImpl = new TraineeDaoStorageImpl(mockStorage);
    }

    @Test
    public void testCreateTrainee() {
        Trainee trainee = new Trainee();
        traineeDaoStorageImpl.create(trainee);
        assertEquals(trainee, traineeDaoStorageImpl.get(1));
    }

    @Test
    public void testSaveTrainee() {
        Trainee trainee = new Trainee();
        trainee.setId(1);
        Trainee trainee2 = new Trainee();
        trainee2.setId(2);
        trainee2.setAddress("Test address");
        traineeDaoStorageImpl.save(trainee);
        traineeDaoStorageImpl.save(trainee2);
        assertEquals(trainee, traineeDaoStorageImpl.get(trainee.getId()));
        assertNotEquals(trainee2, traineeDaoStorageImpl.get(trainee.getId()));
    }

    @Test
    public void testUpdateTrainee() {
        Trainee trainee = new Trainee();
        trainee.setId(1);
        traineeDaoStorageImpl.save(trainee);
        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setId(1);
        updatedTrainee.setAddress("Test address");
        traineeDaoStorageImpl.update(trainee.getId(), updatedTrainee);
        assertEquals(updatedTrainee, traineeDaoStorageImpl.get(trainee.getId()));
    }

    @Test
    public void testDeleteTrainee() {
        Trainee trainee = new Trainee();
        traineeDaoStorageImpl.save(trainee);
        traineeDaoStorageImpl.delete(trainee.getId());
        assertNull(traineeDaoStorageImpl.get(trainee.getId()));
    }

    @Test
    public void testGetTrainee() {
        Trainee trainee = new Trainee();
        traineeDaoStorageImpl.save(trainee);
        assertEquals(trainee, traineeDaoStorageImpl.get(trainee.getId()));
    }

}