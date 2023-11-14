package org.epam.service;

import org.epam.dao.gymDao.TrainerDao;
import org.epam.dao.UserDao;
import org.epam.model.gymModel.TrainingType;
import org.epam.model.gymModel.Trainer;
import org.epam.model.User;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    @Mock
    private static TrainerDao mockTrainerDao = mock(TrainerDao.class);

    @Mock
    private static UserDao mockUserDao = mock(UserDao.class);

    private static TrainerService trainerService = new TrainerService();


    @BeforeAll
    public static void setUp() {
        trainerService.setTrainerDao(mockTrainerDao);
        trainerService.setUserDao(mockUserDao);
    }

    @Test
    public void testCreate() {
        Trainer trainer = new Trainer(TrainingType.CARDIO.getName(), 1);
        String name = "Test";
        String suname = "Trainer";
        when(mockUserDao.setNewUser(name, suname)).thenReturn(new User(1, name, suname, (name+"."+suname), "password01", true));
        when(mockUserDao.create(any(User.class))).thenReturn(1);
        when(mockTrainerDao.get(1)).thenReturn(trainer);
        trainerService.create(TrainingType.CARDIO, "Test", "Trainer");
        assertEquals(trainer, trainerService.select(1));
    }

    @Test
    public void testUpdate() {
        Trainer trainer = new Trainer(TrainingType.CARDIO.getName(), 1);
        String name = "Test";
        String surname = "Trainer";
        when(mockUserDao.setNewUser(name, surname)).thenReturn(new User(1, name, surname, (name+"."+surname), "password01", true));
        when(mockUserDao.create(any(User.class))).thenReturn(1);
        Trainer updatedTrainer = new Trainer(TrainingType.YOGA.getName(), 1);
        when(mockTrainerDao.get(1)).thenReturn(updatedTrainer);
        trainerService.create(TrainingType.CARDIO, "Test", "Trainer");
        trainerService.update(1, updatedTrainer);
        assertEquals(updatedTrainer, trainerService.select(1));
        assertNotEquals(trainer, trainerService.select(1));
    }

    @Test
    public void testSelect() {
        Trainer trainer = new Trainer(TrainingType.CARDIO.getName(), 1);
        when(mockTrainerDao.get(1)).thenReturn(trainer);
        assertEquals(trainer, trainerService.select(1));
    }
}

