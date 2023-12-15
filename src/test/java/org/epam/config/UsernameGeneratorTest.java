package org.epam.config;

import org.epam.Reader;
import org.epam.dao.UserDao;
import org.epam.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsernameGeneratorTest {

    UserDao mock = mock(UserDao.class);

    @InjectMocks
    UsernameGenerator usernameGenerator;

    Reader reader = new Reader();

    @Test
    void testGetDefaultUsername() {
        reader.setStartPath("src/test/resources/models/users/");
        reader.setEndPath(".json");
        User user1 = reader.readEntity("user1", User.class);
        User user2 = reader.readEntity("user1", User.class);
        user2.setUsername(user1.getUsername()+"1");
        User user3 = reader.readEntity("user1", User.class);
        user3.setUsername(user1.getUsername()+"2");
        when(mock.getByUsernameForUsernameGenerator(user1.getUsername())).thenReturn(user1);
        when(mock.getByUsernameForUsernameGenerator(user2.getUsername())).thenReturn(user2);
        when(mock.getByUsernameForUsernameGenerator(user3.getUsername())).thenReturn(user3);
        assertEquals(user1.getUsername()+"3", usernameGenerator.getDefaultUsername(user1.getFirstName(), user1.getLastName()));
    }
}