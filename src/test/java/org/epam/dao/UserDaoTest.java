package org.epam.dao;

import org.epam.config.Storage;
import org.epam.model.User;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDaoTest {

    @Test
    public void testCreateUser() {

        Storage mockStorage = mock(Storage.class);
        when(mockStorage.getUsers()).thenReturn(new HashMap<String, User>());
        UserDao userDao = new UserDao(mockStorage);
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUsername("TestUser");
        int id = userDao.create(user);
        assertEquals(user, userDao.get("TestUser"));
        assertEquals(id, user.getId());
    }

    @Test
    public void testSaveUser() {

        Storage mockStorage = mock(Storage.class);
        when(mockStorage.getUsers()).thenReturn(new HashMap<String, User>());
        UserDao userDao = new UserDao(mockStorage);
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUsername("TestUser");
        userDao.save(user);
        assertEquals(user, userDao.get("TestUser"));
    }

    @Test
    public void testUpdateUser() {
        Storage mockStorage = mock(Storage.class);
        when(mockStorage.getUsers()).thenReturn(new HashMap<String, User>());
        UserDao userDao = new UserDao(mockStorage);
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUsername("TestUser");
        int id = userDao.create(user);
        User updatedUser = new User();
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");
        updatedUser.setUsername("TestUser");
        userDao.update(id, updatedUser);
        updatedUser.setId(id);
        assertEquals(updatedUser, userDao.get("TestUser"));
    }

    @Test
    public void testDeleteUser() {
        Storage mockStorage = mock(Storage.class);
        when(mockStorage.getUsers()).thenReturn(new HashMap<String, User>());
        UserDao userDao = new UserDao(mockStorage);
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUsername("TestUser");
        int id = userDao.create(user);
        userDao.delete("TestUser");
        assertNull(userDao.get("TestUser"));
    }

    @Test
    public void testGetUser() {
        Storage mockStorage = mock(Storage.class);
        when(mockStorage.getUsers()).thenReturn(new HashMap<String, User>());
        UserDao userDao = new UserDao(mockStorage);
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUsername("TestUser");
        int id = userDao.create(user);
        assertEquals(user, userDao.get("TestUser"));
    }


    @Test
    public void testDefaultPassword() {
        Storage mockStorage = mock(Storage.class);
        when(mockStorage.getUsers()).thenReturn(new HashMap<String, User>());
        UserDao userDao = new UserDao(mockStorage);
        String password = userDao.defaultPassword();
        assertNotNull(password);
        assertEquals(10, password.length());
    }

    @Test
    public void testSetNewUser() {
        Storage mockStorage = mock(Storage.class);
        when(mockStorage.getUsers()).thenReturn(new HashMap<String, User>());
        UserDao userDao = new UserDao(mockStorage);
        User user = userDao.setNewUser("Test", "User");
        assertEquals("Test", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertNotNull(user.getUsername());
        assertNotNull(user.getPassword());
        assertTrue(user.isActive());
    }

}