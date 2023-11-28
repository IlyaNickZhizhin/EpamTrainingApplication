package org.epam.service;

import org.epam.Reader;
import org.epam.TestMapper;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.TraineeDaoImpl;
import org.epam.dao.UserDaoImpl;
import org.epam.dto.LoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeProfileResponse;
import org.epam.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.dto.traineeDto.UpdateTraineeProfileRequest;
import org.epam.mapper.TraineeMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDaoImpl mockTraineeDaoImpl = mock(TraineeDaoImpl.class);

    @Mock
    private UserDaoImpl mockUserDao = mock(UserDaoImpl.class);

    @Spy
    private TraineeMapper traineeMapper = TraineeMapper.INSTANCE;

    @Mock
    private PasswordChecker mockPasswordChecker = mock(PasswordChecker.class);

    @InjectMocks
    private TraineeService traineeService;

    TestMapper testMapper = TestMapper.INSTANCE;

    Reader reader = new Reader();
    User user3; User user4; User user5; User user6;
    Trainee trainee3; Trainee trainee4;
    Trainee trainee5; Trainee trainee6;


    @BeforeEach
    public void setUp() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        trainee3 = reader.readEntity("trainees/trainee1", Trainee.class);
        user3 = reader.readEntity("users/user3", User.class);
    }

    @Test
    void testCreate() {
        TraineeRegistrationRequest request =
                reader.readDto("trainees/trainee1", Trainee.class,testMapper::traineeToRegistrationRequest);
        RegistrationResponse response
                = reader.readDto("users/user3", User.class, testMapper::userToRegistrationResponce);
        Trainee trainee = trainee3;
        trainee.setId(0);
        when(mockUserDao.setNewUser(user3.getFirstName(),user3.getLastName())).thenReturn(user3);
        when(mockTraineeDaoImpl.create(trainee)).thenReturn(trainee3);
        assertEquals(response, traineeService.create(request));
    }

    @Test
    void testUpdate() {
        UpdateTraineeProfileRequest request
                = testMapper.traineeToUpdateRequest(trainee3);
        TraineeProfileResponse response
                = traineeMapper.traineeToProfileResponse(trainee3);
        LoginRequest login = testMapper.userToLoginRequest(user3);
        assertEquals(response, traineeService.update(login, request));
    }

    @Test
    void changePassword() {
        
    }

    @Test
    void setActive() {
    }

    @Test
    void delete() {
    }

    @Test
    void selectByUsername() {
    }
}

