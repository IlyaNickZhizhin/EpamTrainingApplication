package org.epam.service;

import org.epam.Reader;
import org.epam.TestMapper;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.UserDao;
import org.epam.dto.LoginRequest;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    @Mock
    private final UserDao userDao = mock(UserDao.class);
    @Spy
    PasswordChecker passwordChecker;
    @InjectMocks
    LoginService loginService;
    Reader reader = new Reader();
    User user1; Trainer trainer1;

    @BeforeEach
    void setUp() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        user1 = reader.readEntity("users/user1", User.class);
        trainer1 = reader.readEntity("trainers/trainer1", Trainer.class);
        user1.setRole(trainer1);
    }

    @Test
    void testLogin() {
        LoginRequest request = TestMapper.INSTANCE.userToLoginRequest(user1);
        when(userDao.getByUsername(request.getUsername())).thenReturn(user1);
        assertTrue(loginService.login(request) instanceof Trainer);
    }
}