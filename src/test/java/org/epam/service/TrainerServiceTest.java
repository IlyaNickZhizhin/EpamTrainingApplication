package org.epam.service;

import org.epam.dao.TrainerDao;
import org.epam.dao.UserDao;
import org.epam.model.TrainingType;
import org.epam.model.Trainer;
import org.epam.model.User;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class TrainerServiceTest {

    @InjectMocks
    TrainerService trainerService;

    @Mock
    TrainerDao trainerDao;

    @Mock
    UserDao userDao;

    @BeforeAll
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreate() {
        TrainingType trainingType = TrainingType.BASIC;
        String firstName = "John";
        String lastName = "Doe";

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);

        Trainer trainer = new Trainer();
        trainer.setSpecialization(trainingType.getName());
        trainer.setUserId(1);

        when(userDao.setNewUser(firstName, lastName)).thenReturn(user);
        when(userDao.save(user)).thenReturn(1);

        Trainer result = trainerService.create(trainingType, firstName, lastName);

        verify(userDao, times(1)).setNewUser(firstName, lastName);
        verify(userDao, times(1)).save(user);
        verify(trainerDao, times(1)).save(trainer);

        assertEquals(trainer, result);
    }

}
