package org.epam.service;

import org.epam.Reader;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.TraineeRepository;
import org.epam.dao.TrainerRepository;
import org.epam.dao.UserRepository;
import org.epam.dto.LoginRequest;
import org.epam.dto.trainerDto.TrainerDto;
import org.epam.mapper.TraineeMapper;
import org.epam.mapper.TrainerMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    @Spy
    PasswordChecker passwordChecker;
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
        when(userDao.findByUsername(request.getUsername())).thenReturn(Optional.of(user1));
        when(traineeDao.findByUser(user1)).thenReturn(Optional.empty());
        when(trainerDao.findByUser(user1)).thenReturn(Optional.of(trainer1));
        assertInstanceOf(TrainerDto.class, loginService.login(request));
    }
}