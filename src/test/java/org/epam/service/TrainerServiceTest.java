package org.epam.service;


import org.epam.Supplier;
import org.epam.dao.TrainerDaoImpl;
import org.epam.dao.UserDaoImpl;
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
    private static TrainerDaoImpl mockTrainerDao= mock(TrainerDaoImpl.class);

    @Mock
    private static UserDaoImpl mockUserDao = mock(UserDaoImpl.class);

    private static TrainerService trainerService = new TrainerService();


    @BeforeAll
    public static void setUp() {
        trainerService.setTrainerDao(mockTrainerDao);
        trainerService.setUserDao(mockUserDao);
    }

    @Test
    public void testCreate() {
        Trainer trainer = Supplier.trainer1;
        when(mockUserDao.setNewUser(Supplier.user1.getFirstName(), Supplier.user1.getLastName()))
                .thenReturn(Supplier.user1);
        when(mockUserDao.create(any(User.class))).thenReturn(Supplier.user1);
        when(mockTrainerDao.get(1)).thenReturn(trainer);
        trainerService.create(Supplier.trainingType1, Supplier.user1.getFirstName(), Supplier.user1.getLastName());
        when(mockTrainerDao.get(1)).thenReturn(trainer);
        assertEquals(trainer, trainerService.select(1));
    }

    @Test
    public void testUpdate() {
        Trainer trainer = Supplier.trainer1;
        String name = Supplier.user1.getFirstName();
        String surname = Supplier.user1.getLastName();
        when(mockUserDao.setNewUser(name, surname)).thenReturn(Supplier.user1);
        when(mockUserDao.create(any(User.class))).thenReturn(Supplier.user1);
        when(mockTrainerDao.get(1)).thenReturn(trainer);
        Trainer updatedTrainer = new Trainer(Supplier.trainer1.getId(), Supplier.trainer1.getSpecialization(), Supplier.user1);
        assertEquals(updatedTrainer, trainerService.select(1));
        updatedTrainer.setSpecialization(Supplier.trainingType2);
        trainerService.update(1, updatedTrainer);
        when(mockTrainerDao.get(1)).thenReturn(updatedTrainer);
        assertEquals(updatedTrainer, trainerService.select(1));
        assertNotEquals(trainer, trainerService.select(1));
    }

    @Test
    public void testSelect() {
        Trainer trainer = Supplier.trainer1;
        when(mockTrainerDao.get(1)).thenReturn(trainer);
        assertEquals(trainer, trainerService.select(1));
    }
}
