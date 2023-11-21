package org.epam.dao.gymStorage.storage;

import org.epam.TestDatabaseInitializer;
import org.epam.config.PasswordGenerator;
import org.epam.config.UsernameGenerator;
import org.epam.dao.UserDaoImpl;
import org.epam.model.User;
import org.epam.testBeans.dao.UserDaoStorageImpl;
import org.epam.testBeans.storageInFile.Storage;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDaoStorageImplTest {

    @Test
    public void testCreateUser() {
        Storage<User> mockStorage = mock(Storage.class);
        UsernameGenerator mockUsernameGenerator = mock(UsernameGenerator.class);
        PasswordGenerator mockPasswordGenerator = mock(PasswordGenerator.class);
        when(mockStorage.getUsers()).thenReturn(new HashMap<>());
        UserDaoStorageImpl userDaoStorageImpl = new UserDaoStorageImpl(mockStorage, mockUsernameGenerator, mockPasswordGenerator);
        User user = TestDatabaseInitializer.user1;
        int id = userDaoStorageImpl.create(user).getId();
        assertEquals(user, userDaoStorageImpl.get(TestDatabaseInitializer.user1.getUsername()));
        assertEquals(id, user.getId());
    }

    @Test
    public void testSaveUser() {
        Storage<User> mockStorage = mock(Storage.class);
        UsernameGenerator mockUsernameGenerator = mock(UsernameGenerator.class);
        PasswordGenerator mockPasswordGenerator = mock(PasswordGenerator.class);
        when(mockStorage.getUsers()).thenReturn(new HashMap<>());
        UserDaoStorageImpl userDaoStorageImpl = new UserDaoStorageImpl(mockStorage, mockUsernameGenerator, mockPasswordGenerator);
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUsername("TestUser");
        userDaoStorageImpl.save(user);
        assertEquals(user, userDaoStorageImpl.get("TestUser"));
    }

    @Test
    public void testUpdateUser() {
        Storage<User> mockStorage = mock(Storage.class);
        UsernameGenerator mockUsernameGenerator = mock(UsernameGenerator.class);
        PasswordGenerator mockPasswordGenerator = mock(PasswordGenerator.class);
        when(mockStorage.getUsers()).thenReturn(new HashMap<>());
        UserDaoStorageImpl userDaoStorageImpl = new UserDaoStorageImpl(mockStorage, mockUsernameGenerator, mockPasswordGenerator);
        User user = TestDatabaseInitializer.user1;
        int id = userDaoStorageImpl.create(user).getId();
        User updatedUser = TestDatabaseInitializer.user1;
        updatedUser.setFirstName("Updated");
        userDaoStorageImpl.update(updatedUser);
        assertEquals(updatedUser, userDaoStorageImpl.get(TestDatabaseInitializer.user1.getUsername()));
    }

    @Test
    public void testDeleteUser() {
        Storage<User> mockStorage = mock(Storage.class);
        UsernameGenerator mockUsernameGenerator = mock(UsernameGenerator.class);
        PasswordGenerator mockPasswordGenerator = mock(PasswordGenerator.class);
        when(mockStorage.getUsers()).thenReturn(new HashMap<>());
        UserDaoStorageImpl userDaoStorageImpl = new UserDaoStorageImpl(mockStorage, mockUsernameGenerator, mockPasswordGenerator);
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUsername("TestUser");
        userDaoStorageImpl.delete("TestUser");
        assertNull(userDaoStorageImpl.get("TestUser"));
    }

    @Test
    public void testGetUser() {
        Storage<User> mockStorage = mock(Storage.class);
        UsernameGenerator mockUsernameGenerator = mock(UsernameGenerator.class);
        PasswordGenerator mockPasswordGenerator = mock(PasswordGenerator.class);
        when(mockStorage.getUsers()).thenReturn(new HashMap<>());
        UserDaoStorageImpl userDaoStorageImpl = new UserDaoStorageImpl(mockStorage, mockUsernameGenerator, mockPasswordGenerator);
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUsername("TestUser");
        userDaoStorageImpl.create(user);
        assertEquals(user, userDaoStorageImpl.get("TestUser"));
    }

    @Test
    public void testSetNewUser() {
        Storage<User> mockStorage = mock(Storage.class);
        UserDaoImpl mockUserDao = mock(UserDaoImpl.class);
        UsernameGenerator mockUsernameGenerator = mock(UsernameGenerator.class);
        PasswordGenerator mockPasswordGenerator = mock(PasswordGenerator.class);
        new UsernameGenerator(mockUserDao);
        when(mockStorage.getUsers()).thenReturn(new HashMap<>());
        UserDaoStorageImpl userDaoStorageImpl = new UserDaoStorageImpl(mockStorage, mockUsernameGenerator, mockPasswordGenerator);
        User user = userDaoStorageImpl.setNewUser("Test", "User");
        assertEquals("Test", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertTrue(user.isActive());
    }

}