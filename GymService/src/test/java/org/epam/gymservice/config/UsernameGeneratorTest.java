package org.epam.gymservice.config;

import org.epam.gymservice.Reader;
import org.epam.gymservice.model.User;
import org.epam.gymservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsernameGeneratorTest {

    UserRepository mock = mock(UserRepository.class);

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
        when(mock.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        when(mock.findByUsername(user2.getUsername())).thenReturn(Optional.of(user2));
        when(mock.findByUsername(user3.getUsername())).thenReturn(Optional.of(user3));
        assertEquals(user1.getUsername()+"3", usernameGenerator.getDefaultUsername(user1.getFirstName(), user1.getLastName()));
    }
}