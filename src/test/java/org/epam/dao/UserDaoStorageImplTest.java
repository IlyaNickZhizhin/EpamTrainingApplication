package org.epam.dao;

import org.epam.Supplier;
import org.epam.config.UsernameGenerator;
import org.epam.dao.storage.UserDaoStorageImpl;
import org.epam.storageInFile.Storage;
import org.epam.model.User;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDaoStorageImplTest {

    @Test
    public void testCreateUser() {
        Storage<User> mockStorage = mock(Storage.class);
        when(mockStorage.getUsers()).thenReturn(new HashMap<>());
        UserDaoStorageImpl userDaoStorageImpl = new UserDaoStorageImpl(mockStorage);
        User user = Supplier.user1;
        int id = userDaoStorageImpl.create(user).getId();
        assertEquals(user, userDaoStorageImpl.get(Supplier.user1.getUsername()));
        assertEquals(id, user.getId());
    }

    @Test
    public void testSaveUser() {
        Storage<User> mockStorage = mock(Storage.class);
        when(mockStorage.getUsers()).thenReturn(new HashMap<>());
        UserDaoStorageImpl userDaoStorageImpl = new UserDaoStorageImpl(mockStorage);
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
        when(mockStorage.getUsers()).thenReturn(new HashMap<>());
        UserDaoStorageImpl userDaoStorageImpl = new UserDaoStorageImpl(mockStorage);
        User user = Supplier.user1;
        int id = userDaoStorageImpl.create(user).getId();
        User updatedUser = Supplier.user1;
        updatedUser.setFirstName("Updated");
        userDaoStorageImpl.update(updatedUser);
        assertEquals(updatedUser, userDaoStorageImpl.get(Supplier.user1.getUsername()));
    }

    @Test
    public void testDeleteUser() {
        Storage<User> mockStorage = mock(Storage.class);
        when(mockStorage.getUsers()).thenReturn(new HashMap<>());
        UserDaoStorageImpl userDaoStorageImpl = new UserDaoStorageImpl(mockStorage);
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
        when(mockStorage.getUsers()).thenReturn(new HashMap<>());
        UserDaoStorageImpl userDaoStorageImpl = new UserDaoStorageImpl(mockStorage);
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
        new UsernameGenerator(mockUserDao);
        when(mockStorage.getUsers()).thenReturn(new HashMap<>());
        UserDaoStorageImpl userDaoStorageImpl = new UserDaoStorageImpl(mockStorage);
        User user = userDaoStorageImpl.setNewUser("Test", "User");
        assertEquals("Test", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertNotNull(user.getUsername());
        assertNotNull(user.getPassword());
        assertTrue(user.isActive());
    }

}