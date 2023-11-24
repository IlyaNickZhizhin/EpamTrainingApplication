package org.epam.config;

import org.epam.Reader;
import org.epam.dao.UserDaoImpl;
import org.epam.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsernameGeneratorTest {

    UserDaoImpl mock = mock(UserDaoImpl.class);

    UsernameGenerator usernameGenerator = new UsernameGenerator(mock);

    Reader reader = new Reader();

    @Test
    void testGetDefaultUsername() {
        reader.setStartPath("src/test/resources/models/users/");
        reader.setEndPath(".json");
        User user1 = reader.readUser("user1");
        User user2 = reader.readUser("user1");
        user2.setUsername(user1.getUsername()+"1");
        User user3 = reader.readUser("user1");
        user3.setUsername(user1.getUsername()+"2");
        when(mock.getByUsernameForUsernameGenerator(user1.getUsername())).thenReturn(user1);
        when(mock.getByUsernameForUsernameGenerator(user2.getUsername())).thenReturn(user2);
        when(mock.getByUsernameForUsernameGenerator(user3.getUsername())).thenReturn(user3);
        assertEquals(user1.getUsername()+"3", usernameGenerator.getDefaultUsername(user1.getFirstName(), user1.getLastName()));
    }
}