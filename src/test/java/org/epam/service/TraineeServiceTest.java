package org.epam.service;

import org.epam.Reader;
import org.epam.TestMapper;
import org.epam.dao.TraineeDaoImpl;
import org.epam.dao.UserDao;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeProfileResponse;
import org.epam.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.dto.traineeDto.UpdateTraineeProfileRequest;
import org.epam.mapper.TraineeMapper;
import org.epam.mapper.TrainerMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {
    @Mock
    private TraineeDaoImpl mockTraineeDaoImpl = mock(TraineeDaoImpl.class);
    @Mock
    private UserDao mockUserDao = mock(UserDao.class);
    @Spy
    TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);
    @Spy
    @InjectMocks
    TraineeMapper traineeMapper = Mappers.getMapper(TraineeMapper.class);
    @InjectMocks
    private TraineeService traineeService;

    TestMapper testMapper = TestMapper.INSTANCE;

    Reader reader = new Reader();
    User user3; User user4; User user5;
    Trainee trainee3; Trainee trainee4;
    Trainee trainee5;


    @BeforeEach
    public void setUp() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        trainee3 = reader.readEntity("trainees/trainee3", Trainee.class);
        trainee4 = reader.readEntity("trainees/trainee4", Trainee.class);
        trainee5 = reader.readEntity("trainees/trainee5", Trainee.class);
        user3 = reader.readEntity("users/user3", User.class);
        user3.setRole(trainee3);
        user4 = reader.readEntity("users/user4", User.class);
        user4.setRole(trainee4);
        user5 = reader.readEntity("users/user5", User.class);
        user5.setRole(trainee5);
    }

    @Test
    void testCreate() {
        TraineeRegistrationRequest request =
                reader.readDto("trainees/trainee3", Trainee.class,testMapper::traineeToRegistrationRequest);
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
        Trainee trainee = reader.readEntity("trainees/trainee3", Trainee.class);
        trainee.getUser().setFirstName("user5");
        UpdateTraineeProfileRequest request
                = testMapper.traineeToUpdateRequest(trainee3);
        request.setFirstname("user5");
        TraineeProfileResponse response
                = traineeMapper.traineeToProfileResponse(trainee3);
        response.setFirstname("user5");
        when(mockUserDao.getByUsername(request.getUsername())).thenReturn(user5);
        when(mockTraineeDaoImpl.getModelByUser(user5)).thenReturn(trainee3);
        when(mockTraineeDaoImpl.update(3, trainee)).thenReturn(trainee);
        assertEquals(response, traineeService.update(request));
    }
    @Test
    void selectByUsername() {
        TraineeProfileResponse response = traineeMapper.traineeToProfileResponse(trainee3);
        when(mockUserDao.getByUsername(user3.getUsername())).thenReturn(user3);
        when(mockTraineeDaoImpl.getModelByUser(user3)).thenReturn(trainee3);
        assertEquals(response, traineeService.selectByUsername(user3.getUsername()));
    }
    @Test
    void changePassword() {
        ChangeLoginRequest request
                = reader.readDto("trainees/trainee4", Trainee.class, testMapper::roleToChangeLoginRequest);
        request.setNewPassword("newPassword");
        User userNew = reader.readEntity("users/user5", User.class);
        userNew.setPassword("newPassword");
        when(mockUserDao.getByUsername(request.getUsername())).thenReturn(user5);
        when(mockTraineeDaoImpl.getModelByUser(user5)).thenReturn(trainee5);
        when(mockUserDao.update(any(Integer.class), any(User.class))).thenReturn(userNew);
        assertTrue(traineeService.changePassword(request));
    }

    @Test
    void setActive() {
        ActivateDeactivateRequest request =
                reader.readDto("trainees/trainee4", Trainee.class, testMapper::roleToActivateDeactivateRequest);
        request.setActive(false);
        user4.setActive(false);
        when(mockUserDao.getByUsername(request.getUsername())).thenReturn(user4);
        when(mockTraineeDaoImpl.getModelByUser(user4)).thenReturn(trainee4);
        assertFalse(traineeService.setActive(request));
    }

    @Test
    void delete() {
        when(mockUserDao.getByUsername(user3.getUsername())).thenReturn(user3);
        when(mockUserDao.delete(user3.getId())).thenReturn(user3);
        assertTrue(traineeService.delete(user3.getUsername()));
        verify(mockUserDao).delete(user3.getId());
    }
}

