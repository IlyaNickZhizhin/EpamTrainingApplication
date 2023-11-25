package org.epam.service;

import org.epam.Reader;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.TraineeDaoImpl;
import org.epam.dao.UserDaoImpl;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.epam.TestDatabaseInitializer.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class TraineeServiceTest {

    @Mock
    private TraineeDaoImpl mockTraineeDaoImpl = mock(TraineeDaoImpl.class);

    @Mock
    private UserDaoImpl mockUserDao = mock(UserDaoImpl.class);

    @Mock
    private PasswordChecker mockPasswordChecker = mock(PasswordChecker.class);

    @InjectMocks
    private  TraineeService traineeService = new TraineeService();

    Reader reader = new Reader();

    @BeforeEach
    public void setUp() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        initMocks(this);
        traineeService.setTraineeDao(mockTraineeDaoImpl);
    }

    @Test
    public void testCreate(){
        Trainee trainee = reader.readTrainee("trainees/trainee1");
        User user = reader.readUser("users/user1");
        String name = user.getFirstName();
        String suname = user.getLastName();
        when(mockUserDao.setNewUser(name, suname)).thenReturn(user3);
        when(mockTraineeDaoImpl.get(1)).thenReturn(trainee);
        traineeService.create(trainee3_FirstName, trainee3_LastName);
        assertEquals(trainee, traineeService.select(trainee3_Username, trainee3_Password, 1));
    }

    @Test
    public void testCreateFull(){
        Trainee trainee = trainee3;
        when(mockUserDao.setNewUser(trainee3_FirstName, trainee3_LastName)).thenReturn(user3);
        when(mockTraineeDaoImpl.get(1)).thenReturn(trainee);
        traineeService.create(trainee3_FirstName, trainee3_LastName, trainee3_Address, trainee3_Birthday);
        assertEquals(trainee, traineeService.select(trainee3_Username, trainee3_Password, 1));
    }

    @Test
    public void testCreateBirthday(){
        when(mockUserDao.setNewUser(trainee5_FirstName, trainee5_LastName)).thenReturn(user5);
        when(mockTraineeDaoImpl.get(3)).thenReturn(trainee5);
        traineeService.create(trainee5_FirstName, trainee5_LastName, trainee5_Birthday);
        assertEquals(trainee5, traineeService.select(trainee5_Username, trainee5_Password, 3));
    }

    @Test
    public void testCreateAdress(){
        when(mockUserDao.setNewUser(trainee6_FirstName, trainee6_LastName)).thenReturn(user6);
        when(mockTraineeDaoImpl.get(4)).thenReturn(trainee6);
        traineeService.create(trainee6_FirstName, trainee6_LastName, trainee6_Address);
        assertEquals(trainee6, traineeService.select(trainee6_Username, trainee6_Password, 4));
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
        when(mockTraineeDaoImpl.get(1)).thenReturn(updatedTrainee);
        traineeService.create("Test", "Trainee");
        traineeService.update((name+"."+surname), "password01", 1, updatedTrainee);
        assertEquals(updatedTrainee, traineeService.select((name+"."+surname), "password01", 1 ));
        assertNotEquals(trainee, traineeService.select((name+"."+surname), "password01", 1 ));
    }

    @Test
    public void testDelete() {
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
        when(mockTraineeDaoImpl.get(1)).thenReturn(trainee);
        assertEquals(trainee, traineeService.select( TestDatabaseInitializer.user1.getUsername(), TestDatabaseInitializer.user1.getPassword(),1));
    }

    @Test
    public void testSelectByUsername() {
        when(mockUserDao.getByUsername(trainee3_Username)).thenReturn(user3);
        when(mockTraineeDaoImpl.getModelByUser(user3)).thenReturn(trainee3);
        assertEquals(trainee3, traineeService.selectByUsername(trainee3_Username, trainee3_Password));
    }

    @Test
    public void testChangePassword() {
        User upDated = new User(6, trainee6_FirstName, trainee6_LastName, trainee6_Username, trainee6_Password, true);
        when(mockUserDao.getByUsername(trainee6_Username)).thenReturn(user6);
        doNothing().when(mockUserDao).update(user6.getId(), upDated);
        traineeService.changePassword(trainee6_Username, trainee6_Password, trainee4_Password);
        verify(mockPasswordChecker, times(1)).checkPassword(trainee6_Username, trainee6_Password, user6);
    }

}

