package org.epam.service.storageService;

import org.epam.Supplier;
import org.epam.dao.storage.UserDaoStorageImpl;
import org.epam.dao.storage.gymDaoFileStorage.TraineeDaoStorageImpl;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.service.storage.TraineeStorageService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TraineeStorageServiceTest {

    @Mock
    private static TraineeDaoStorageImpl mockTraineeDaoStorageImpl = mock(TraineeDaoStorageImpl.class);

    @Mock
    private static UserDaoStorageImpl mockUserDaoStorageImpl = mock(UserDaoStorageImpl.class);


    private static final TraineeStorageService traineeService = new TraineeStorageService();

    @BeforeAll
    public static void setUp() {
        traineeService.setTraineeDao(mockTraineeDaoStorageImpl);
        traineeService.setUserDao(mockUserDaoStorageImpl);
    }

    @Test
    public void testCreate() {
        Trainee trainee = new Trainee();
        trainee.setId(1);
        String name = "Test";
        String suname = "Trainee";
        when(mockUserDaoStorageImpl.setNewUser(name, suname)).thenReturn(new User(1, name, suname, (name+"."+suname), "password01", true));
        when(mockTraineeDaoStorageImpl.get(1)).thenReturn(trainee);
        traineeService.create("Test", "Trainee");
        assertEquals(trainee, traineeService.select(1));
    }

    @Test
    public void testUpdate() {
        Trainee trainee = new Trainee();
        trainee.setId(1);
        String name = "Test";
        String surname = "Trainee";
        when(mockUserDaoStorageImpl.setNewUser(name, surname)).thenReturn(new User(1, name, surname, (name+"."+surname), "password01", true));
        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setAddress("Test address");
        when(mockTraineeDaoStorageImpl.get(1)).thenReturn(updatedTrainee);
        traineeService.create("Test", "Trainee");
        traineeService.update(1, updatedTrainee);
        assertEquals(updatedTrainee, traineeService.select(1));
        assertNotEquals(trainee, traineeService.select(1));
    }

    @Test
    public void testDelete() {
        Trainee trainee = Supplier.trainee4;
        String name = Supplier.user4.getFirstName();
        String surname = Supplier.user4.getLastName();
        when(mockUserDaoStorageImpl.setNewUser(name, surname)).thenReturn(Supplier.user4);
        when(mockUserDaoStorageImpl.create(any(User.class))).thenReturn(Supplier.user4);
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
