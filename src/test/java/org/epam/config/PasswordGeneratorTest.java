package org.epam.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PasswordGeneratorTest {

    @Test
    public void testDefaultPassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        String password = passwordGenerator.getDefaultPassword();
        assertNotNull(password);
        assertEquals(10, password.length());
    }
}