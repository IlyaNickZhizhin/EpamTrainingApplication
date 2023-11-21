package org.epam.service;


import org.epam.TestDatabaseInitializer;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.TrainerDaoImpl;
import org.epam.dao.UserDaoImpl;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import static org.epam.TestDatabaseInitializer.trainee3;
import static org.epam.TestDatabaseInitializer.trainee3_Password;
import static org.epam.TestDatabaseInitializer.trainee3_Username;
import static org.epam.TestDatabaseInitializer.trainee4_Password;
import static org.epam.TestDatabaseInitializer.trainer1;
import static org.epam.TestDatabaseInitializer.trainer1_Password;
import static org.epam.TestDatabaseInitializer.trainer1_Username;
import static org.epam.TestDatabaseInitializer.trainer2;
import static org.epam.TestDatabaseInitializer.trainer2_FirstName;
import static org.epam.TestDatabaseInitializer.trainer2_LastName;
import static org.epam.TestDatabaseInitializer.trainer2_Password;
import static org.epam.TestDatabaseInitializer.trainer2_Username;
import static org.epam.TestDatabaseInitializer.user1;
import static org.epam.TestDatabaseInitializer.user2;
import static org.epam.TestDatabaseInitializer.user3;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class TrainerServiceTest {

    @Mock
    private TrainerDaoImpl mockTrainerDao= mock(TrainerDaoImpl.class);

    @Mock
    private UserDaoImpl mockUserDao = mock(UserDaoImpl.class);

    @Mock
    private PasswordChecker mockPasswordChecker = mock(PasswordChecker.class);

    @InjectMocks
    private TrainerService trainerService;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        trainerService.setTrainerDao(mockTrainerDao);
    }


    @Test
    public void testCreate() throws AccessDeniedException {
        Trainer trainer = trainer1;
        when(mockUserDao.setNewUser(user1.getFirstName(), user1.getLastName()))
                .thenReturn(user1);
        when(mockUserDao.create(any(User.class))).thenReturn(user1);
        when(mockTrainerDao.get(1)).thenReturn(trainer);
        trainerService.create(user1.getFirstName(), user1.getLastName(), TestDatabaseInitializer.trainingType1);
        when(mockTrainerDao.get(1)).thenReturn(trainer);
        assertEquals(trainer, trainerService.select(trainer1.getUser().getUsername(), trainer1.getUser().getPassword(), 1));
    }

    @Test
    public void testUpdate() throws AccessDeniedException {
        Trainer trainer = trainer1;
        String name = user1.getFirstName();
        String surname = user1.getLastName();
        when(mockUserDao.setNewUser(name, surname)).thenReturn(user1);
        when(mockUserDao.create(any(User.class))).thenReturn(user1);
        when(mockTrainerDao.get(1)).thenReturn(trainer);
        Trainer updatedTrainer = new Trainer(trainer1.getId(), trainer1.getSpecialization(), user1);
        assertEquals(updatedTrainer, trainerService.select(trainer1.getUser().getUsername(), trainer1.getUser().getPassword(), 1));
        updatedTrainer.setSpecialization(TestDatabaseInitializer.trainingType2);
        trainerService.update(trainer1.getUser().getUsername(), trainer1.getUser().getPassword(), 1, updatedTrainer);
        when(mockTrainerDao.get(1)).thenReturn(updatedTrainer);
        assertEquals(updatedTrainer, trainerService.select(trainer1.getUser().getUsername(), trainer1.getUser().getPassword(), 1));
        assertNotEquals(trainer, trainerService.select(trainer1.getUser().getUsername(), trainer1.getUser().getPassword(), 1));
    }

    @Test
    public void testSelect() throws AccessDeniedException {
        Trainer trainer = trainer1;
        when(mockTrainerDao.get(1)).thenReturn(trainer);
        assertEquals(trainer, trainerService.select(trainer1.getUser().getUsername(), trainer1.getUser().getPassword(), 1));
    }

    @Test
    public void testSelectAll() {
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(trainer1);
        trainers.add(trainer2);
        when(mockTrainerDao.getAll()).thenReturn(trainers);
        assertEquals(trainers, trainerService.selectAll(trainer1_Username, trainer1_Password));
    }

    @Test
    public void testSelectByUsername() {
        when(mockUserDao.getByUsername(trainer2_Username)).thenReturn(user2);
        when(mockTrainerDao.getByUserId(user2.getId())).thenReturn(trainer2);
        assertEquals(trainer2, trainerService.selectByUsername(trainer2_Username, trainer2_Password));
    }

    @Test
    public void testChangePassword() {
        User upDated = new User(3, trainer2_FirstName, trainer2_LastName, trainer2_Username, trainer2_Password, true);
        when(mockUserDao.getByUsername(trainee3_Username)).thenReturn(user3);
        doNothing().when(mockUserDao).update(user3.getId(), upDated);
        trainerService.changePassword(trainee3_Username, trainee3_Password, trainee4_Password);
        verify(mockPasswordChecker, times(1)).checkPassword(trainee3_Username, trainee3_Password);
    }

    @Test
    public void testSetActiveSucssed() {
        User user = new User(2, trainer2_FirstName, trainer2_LastName, trainer2_Username, trainer2_Password, true);
        when(mockUserDao.getByUsername(trainer2_Username)).thenReturn(user);
        assertNotEquals(user, trainee3);
    }
}
