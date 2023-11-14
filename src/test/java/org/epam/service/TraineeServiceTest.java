package org.epam.service;

import org.epam.dao.UserDao;
import org.epam.dao.gymDao.TraineeDao;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TraineeServiceTest {

    @Mock
    private static TraineeDao mockTraineeDao = mock(TraineeDao.class);

    @Mock
    private static UserDao mockUserDao = mock(UserDao.class);

    private static final TraineeService traineeService = new TraineeService();

    @BeforeAll
    public static void setUp() {
        traineeService.setTraineeDao(mockTraineeDao);
        traineeService.setUserDao(mockUserDao);
    }

    @Test
    public void testCreate() {
        Trainee trainee = new Trainee();
        trainee.setId(1);
        String name = "Test";
        String suname = "Trainee";
        when(mockUserDao.setNewUser(name, suname)).thenReturn(new User(1, name, suname, (name+"."+suname), "password01", true));
        when(mockTraineeDao.get(1)).thenReturn(trainee);
        traineeService.create("Test", "Trainee");
        assertEquals(trainee, traineeService.select(1));
    }

    @Test
    public void testUpdate() {
        Trainee trainee = new Trainee();
        trainee.setId(1);
        String name = "Test";
        String surname = "Trainee";
        when(mockUserDao.setNewUser(name, surname)).thenReturn(new User(1, name, surname, (name+"."+surname), "password01", true));
        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setAddress("Test address");
        when(mockTraineeDao.get(1)).thenReturn(updatedTrainee);
        traineeService.create("Test", "Trainee");
        traineeService.update(1, updatedTrainee);
        assertEquals(updatedTrainee, traineeService.select(1));
        assertNotEquals(trainee, traineeService.select(1));
    }

    @Test
    public void testDelete() {
        Trainee trainee = new Trainee();
        trainee.setId(2);
        String name = "Test2";
        String surname = "Trainee2";
        when(mockUserDao.setNewUser(name, surname)).thenReturn(new User(2, name, surname, (name+"."+surname), "password01", true));
        when(mockUserDao.create(any(User.class))).thenReturn(2);
        traineeService.create(name, surname);
        traineeService.delete(2);
        assertNull(traineeService.select(2));
    }

    @Test
    public void testSelect() {
        Trainee trainee = new Trainee();
        trainee.setId(1);
        assertEquals(trainee, traineeService.select(1));
    }
}
