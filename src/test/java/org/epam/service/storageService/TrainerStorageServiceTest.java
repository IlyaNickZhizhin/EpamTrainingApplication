package org.epam.service.storageService;

import org.epam.TestDatabaseInitializer;
import org.epam.testBeans.dao.UserDaoStorageImpl;
import org.epam.testBeans.dao.gymDaoFileStorage.TrainerDaoStorageImpl;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.epam.testBeans.service.TrainerStorageService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        Trainer trainer = TestDatabaseInitializer.trainer1;
        when(mockUserDaoStorageImpl.setNewUser(TestDatabaseInitializer.user1.getFirstName(), TestDatabaseInitializer.user1.getLastName()))
                .thenReturn(TestDatabaseInitializer.user1);
        when(mockUserDaoStorageImpl.create(any(User.class))).thenReturn(TestDatabaseInitializer.user1);
        when(mockTrainerDaoStorageImpl.get(1)).thenReturn(trainer);
        trainerService.create(TestDatabaseInitializer.trainingType1, TestDatabaseInitializer.user1.getFirstName(), TestDatabaseInitializer.user1.getLastName());
        assertEquals(trainer, trainerService.select(1));
    }

    @Test
    public void testUpdate() {
        Trainer trainer = TestDatabaseInitializer.trainer1;
        String name = TestDatabaseInitializer.user1.getFirstName();
        String surname = TestDatabaseInitializer.user1.getLastName();
        when(mockUserDaoStorageImpl.setNewUser(name, surname)).thenReturn(TestDatabaseInitializer.user1);
        when(mockUserDaoStorageImpl.create(any(User.class))).thenReturn(TestDatabaseInitializer.user1);
        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setSpecialization(TestDatabaseInitializer.trainingType2);
        when(mockTrainerDaoStorageImpl.get(1)).thenReturn(updatedTrainer);
        trainerService.create(TestDatabaseInitializer.trainingType2, "Test", "Trainer");
        trainerService.update(1, updatedTrainer);
        assertEquals(updatedTrainer, trainerService.select(1));
        assertNotEquals(trainer, trainerService.select(1));
    }

    @Test
    public void testSelect() {
        Trainer trainer = TestDatabaseInitializer.trainer1;
        when(mockTrainerDaoStorageImpl.get(1)).thenReturn(trainer);
        assertEquals(trainer, trainerService.select(1));
    }
}

