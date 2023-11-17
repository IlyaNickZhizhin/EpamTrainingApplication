package org.epam.service.storageService;

import org.epam.Supplier;
import org.epam.dao.storage.gymDaoFileStorage.TrainerDaoStorageImpl;
import org.epam.dao.storage.UserDaoStorageImpl;
import org.epam.model.gymModel.Trainer;
import org.epam.model.User;

import org.epam.service.storage.TrainerStorageService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

class TrainerStorageServiceTest {

    @Mock
    private static TrainerDaoStorageImpl mockTrainerDaoStorageImpl = mock(TrainerDaoStorageImpl.class);

    @Mock
    private static UserDaoStorageImpl mockUserDaoStorageImpl = mock(UserDaoStorageImpl.class);

    private static TrainerStorageService trainerService = new TrainerStorageService();


    @BeforeAll
    public static void setUp() {
        trainerService.setTrainerDao(mockTrainerDaoStorageImpl);
        trainerService.setUserDao(mockUserDaoStorageImpl);
    }

    @Test
    public void testCreate() {
        Trainer trainer = Supplier.trainer1;
        when(mockUserDaoStorageImpl.setNewUser(Supplier.user1.getFirstName(), Supplier.user1.getLastName()))
                .thenReturn(Supplier.user1);
        when(mockUserDaoStorageImpl.create(any(User.class))).thenReturn(Supplier.user1);
        when(mockTrainerDaoStorageImpl.get(1)).thenReturn(trainer);
        trainerService.create(Supplier.trainingType1, Supplier.user1.getFirstName(), Supplier.user1.getLastName());
        assertEquals(trainer, trainerService.select(1));
    }

    @Test
    public void testUpdate() {
        Trainer trainer =Supplier.trainer1;
        String name = Supplier.user1.getFirstName();
        String surname = Supplier.user1.getLastName();
        when(mockUserDaoStorageImpl.setNewUser(name, surname)).thenReturn(Supplier.user1);
        when(mockUserDaoStorageImpl.create(any(User.class))).thenReturn(Supplier.user1);
        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setSpecialization(Supplier.trainingType2);
        when(mockTrainerDaoStorageImpl.get(1)).thenReturn(updatedTrainer);
        trainerService.create(Supplier.trainingType2, "Test", "Trainer");
        trainerService.update(1, updatedTrainer);
        assertEquals(updatedTrainer, trainerService.select(1));
        assertNotEquals(trainer, trainerService.select(1));
    }

    @Test
    public void testSelect() {
        Trainer trainer = Supplier.trainer1;
        when(mockTrainerDaoStorageImpl.get(1)).thenReturn(trainer);
        assertEquals(trainer, trainerService.select(1));
    }
}

