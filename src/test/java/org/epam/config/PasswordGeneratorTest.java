package org.epam.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class PasswordGeneratorTest {

    @Test
    public void testDefaultPassword() {
        PasswordGenerator mockGenerator = mock(PasswordGenerator.class);
        String password = mockGenerator.getDefaultPassword();
        assertNotNull(password);
        assertEquals(10, password.length());
    }
}