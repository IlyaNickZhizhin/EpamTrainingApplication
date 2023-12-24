package org.epam.service;

import org.epam.Reader;
import org.epam.dto.LoginRequest;
import org.epam.mapper.TraineeMapper;
import org.epam.mapper.TrainerMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.epam.repository.TraineeRepository;
import org.epam.repository.TrainerRepository;
import org.epam.repository.UserRepository;
import org.epam.service.security.JwtService;
import org.epam.service.security.LoginAttemptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    @Mock
    private final UserRepository userDao = mock(UserRepository.class);
    @Mock
    private final TrainerRepository trainerDao = mock(TrainerRepository.class);
    @Mock
    private final TraineeRepository traineeDao = mock(TraineeRepository.class);
    @Mock
    private final LoginAttemptService loginAttemptService = mock(LoginAttemptService.class);
    @Mock
    private final JwtService jwtService = mock(JwtService.class);
    @Mock
    private AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    @Spy
    private final TraineeMapper traineeMapper = Mappers.getMapper(TraineeMapper.class);
    @Spy
    private final TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);
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
    }

    @Test
    void testLogin() {
        LoginRequest request = Mappers.getMapper(TraineeMapper.class).userToLoginRequest(user1);
        when(userDao.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        when(loginAttemptService.isBlocked(user1.getUsername())).thenReturn(false);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()))).thenReturn(null);
        when(jwtService.generateToken(user1)).thenReturn("Authorized");
        assertEquals("Authorized", loginService.login(request));
    }
}