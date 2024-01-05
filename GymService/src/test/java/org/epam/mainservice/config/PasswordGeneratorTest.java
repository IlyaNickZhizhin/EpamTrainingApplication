package org.epam.mainservice.config;

import org.epam.gymservice.config.PasswordGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PasswordGeneratorTest {

    @Test
    public void testDefaultPassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        String password = passwordGenerator.getDefaultPassword();
        assertNotNull(password);
        assertEquals(10, password.length());
    }
}