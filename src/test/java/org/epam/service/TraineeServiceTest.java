package org.epam.service;

import org.epam.TestDatabaseInitializer;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.TraineeDaoImpl;
import org.epam.dao.UserDaoImpl;
import org.epam.exceptions.InvalidDataException;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.epam.TestDatabaseInitializer.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class TraineeServiceTest {

    @Mock
    private TraineeDaoImpl mockTraineeDao = mock(TraineeDaoImpl.class);

    @Mock
    private UserDaoImpl mockUserDao = mock(UserDaoImpl.class);

    @Mock
    private PasswordChecker mockPasswordChecker = mock(PasswordChecker.class);

    @InjectMocks
    private  TraineeService traineeService = new TraineeService();

    @BeforeEach
    public void setUp() {
        initMocks(this);
        traineeService.setTraineeDao(mockTraineeDao);
    }

    @Test
    public void testCreate(){
        Trainee trainee = trainee3;
        String name = trainee3_FirstName;
        String suname = trainee3_LastName;
        when(mockUserDao.setNewUser(name, suname)).thenReturn(user3);
        when(mockTraineeDao.get(1)).thenReturn(trainee);
        traineeService.create(trainee3_FirstName, trainee3_LastName);
        assertEquals(trainee, traineeService.select(trainee3_Username, trainee3_Password, 1));
    }

    @Test
    public void testCreateFull(){
        Trainee trainee = trainee3;
        String name = trainee3_FirstName;
        String suname = trainee3_LastName;
        LocalDate date = trainee3_Birthday;
        String address = trainee3_Address;
        when(mockUserDao.setNewUser(name, suname)).thenReturn(user3);
        when(mockTraineeDao.get(1)).thenReturn(trainee);
        traineeService.create(trainee3_FirstName, trainee3_LastName, address, date);
        assertEquals(trainee, traineeService.select(trainee3_Username, trainee3_Password, 1));
    }

    @Test
    public void testCreateBirthday(){
        Trainee trainee = trainee5;
        String name = trainee5_FirstName;
        String suname = trainee5_LastName;
        LocalDate date = trainee5_Birthday;
        when(mockUserDao.setNewUser(name, suname)).thenReturn(user5);
        when(mockTraineeDao.get(3)).thenReturn(trainee);
        traineeService.create(trainee5_FirstName, trainee5_LastName, date);
        assertEquals(trainee, traineeService.select(trainee5_Username, trainee5_Password, 3));
    }

    @Test
    public void testCreateAdress(){
        Trainee trainee = trainee6;
        String name = trainee6_FirstName;
        String suname = trainee6_LastName;
        String address = trainee6_Address;
        when(mockUserDao.setNewUser(name, suname)).thenReturn(user6);
        when(mockTraineeDao.get(4)).thenReturn(trainee);
        traineeService.create(trainee6_FirstName, trainee6_LastName, address);
        assertEquals(trainee, traineeService.select(trainee6_Username, trainee6_Password, 4));
    }

    @Test
    public void testCreateWrongOrder(){
        when(mockUserDao.setNewUser(trainee3_FirstName, trainee3_LastName)).thenReturn(user3);
        when(mockTraineeDao.get(1)).thenReturn(trainee3);
        assertThrows(InvalidDataException.class, () -> traineeService.create(trainee3_FirstName, trainee3_LastName, trainee3_Birthday, trainee3_Address));
    }

    @Test
    public void testUpdate(){
        Trainee trainee = new Trainee();
        trainee.setId(1);
        String name = "Test";
        String surname = "Trainee";
        when(mockUserDao.setNewUser(name, surname)).thenReturn(new User(1, name, surname, (name+"."+surname), "password01", true));
        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setAddress("Test address");
        when(mockTraineeDao.get(1)).thenReturn(updatedTrainee);
        traineeService.create("Test", "Trainee");
        traineeService.update((name+"."+surname), "password01", 1, updatedTrainee);
        assertEquals(updatedTrainee, traineeService.select((name+"."+surname), "password01", 1 ));
        assertNotEquals(trainee, traineeService.select((name+"."+surname), "password01", 1 ));
    }

    @Test
    public void testDelete() {
        Trainee trainee = trainee4;
        String name = user4.getFirstName();
        String surname = user4.getLastName();
        when(mockUserDao.setNewUser(name, surname)).thenReturn(user4);
        when(mockUserDao.create(any(User.class))).thenReturn(user4);
        traineeService.create(name, surname);
        traineeService.delete(user4.getUsername(), user4.getPassword(), 2);
        assertNull(traineeService.select(user4.getUsername(), user4.getPassword(), 2));
    }

    @Test
    public void testSelect() {
        Trainee trainee = new Trainee();
        trainee.setId(1);
        trainee.setUser(TestDatabaseInitializer.user1);
        when(mockTraineeDao.get(1)).thenReturn(trainee);
        assertEquals(trainee, traineeService.select( TestDatabaseInitializer.user1.getUsername(), TestDatabaseInitializer.user1.getPassword(),1));
    }

    @Test
    public void testSelectByUsername() {
        when(mockUserDao.getByUsername(trainee3_Username)).thenReturn(user3);
        when(mockTraineeDao.getByUserId(user3.getId())).thenReturn(trainee3);
        assertEquals(trainee3, traineeService.selectByUsername(trainee3_Username, trainee3_Password));
    }

    @Test
    public void testChangePassword() {
        User upDated = new User(6, trainee6_FirstName, trainee6_LastName, trainee6_Username, trainee6_Password, true);
        when(mockUserDao.getByUsername(trainee6_Username)).thenReturn(user6);
        doNothing().when(mockUserDao).update(user6.getId(), upDated);
        traineeService.changePassword(trainee6_Username, trainee6_Password, trainee4_Password);
        verify(mockPasswordChecker, times(1)).checkPassword(trainee6_Username, trainee6_Password);
    }

}

