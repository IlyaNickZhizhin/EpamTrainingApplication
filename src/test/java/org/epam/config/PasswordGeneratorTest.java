package org.epam.config;

import org.epam.dao.UserDao;
import org.epam.model.User;
import org.epam.storageInFile.Storage;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PasswordGeneratorTest {

    @Test
    public void testDefaultPassword() {
        PasswordGenerator mockGenerator = mock(PasswordGenerator.class);
        String password = mockGenerator.getDefaultPassword();
        assertNotNull(password);
        assertEquals(10, password.length());
    }
}