package org.epam.service;

import org.epam.Reader;
import org.epam.dao.TraineeRepository;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeProfileResponse;
import org.epam.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.dto.traineeDto.UpdateTraineeProfileRequest;
import org.epam.dto.trainingDto.GetTraineeTrainingsListRequest;
import org.epam.dto.trainingDto.GetTrainingsResponse;
import org.epam.mapper.TraineeMapper;
import org.epam.mapper.TrainerMapper;
import org.epam.mapper.TrainingMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {
    @Mock
    private TraineeRepository mockTraineeDaoImpl = mock(TraineeRepository.class);
    @Mock
    private UserService mockUserDao = mock(UserService.class);
    @Spy
    TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);
    @Spy
    TraineeMapper traineeMapper = Mappers.getMapper(TraineeMapper.class);
    @Spy
    @InjectMocks
    TrainingMapper trainingMapper = Mappers.getMapper(TrainingMapper.class);
    @InjectMocks
    private TraineeService traineeService;

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
        user4 = reader.readEntity("users/user4", User.class);
        user5 = reader.readEntity("users/user5", User.class);
    }

    @Test
    void testCreate() {
        TraineeRegistrationRequest request =
                reader.readDto("trainees/trainee3", Trainee.class,traineeMapper::traineeToRegistrationRequest);
        RegistrationResponse response
                = reader.readDto("users/user3", User.class, traineeMapper::userToRegistrationResponce);
        Trainee trainee = trainee3;
        trainee.setId(0);
        when(mockUserDao.setNewUser(user3.getFirstName(),user3.getLastName())).thenReturn(Optional.ofNullable(user3));
        when(mockTraineeDaoImpl.save(trainee)).thenReturn(trainee3);
        assertEquals(response, traineeService.create(request));
    }

    @Test
    void testUpdate() {
        Trainee trainee = reader.readEntity("trainees/trainee3", Trainee.class);
        User user = reader.readEntity("users/user3", User.class);
        user.setFirstName("user5");
        trainee.getUser().setFirstName("user5");
        UpdateTraineeProfileRequest request
                = traineeMapper.traineeToUpdateRequest(trainee3);
        request.setFirstname("user5");
        TraineeProfileResponse response
                = traineeMapper.traineeToProfileResponse(trainee3);
        response.setFirstname("user5");
        when(mockUserDao.findByUsername(request.getUsername())).thenReturn(Optional.ofNullable(user3));
        when(mockTraineeDaoImpl.findByUser(user3)).thenReturn(Optional.ofNullable(trainee3));
        when(mockUserDao.update(any(Integer.class), any(User.class))).thenReturn(Optional.of(user));
        when(mockTraineeDaoImpl.save(trainee)).thenReturn(trainee);
        assertEquals(response, traineeService.update(user.getUsername(), request));
    }
    @Test
    void selectByUsername() {
        TraineeProfileResponse response = traineeMapper.traineeToProfileResponse(trainee3);
        when(mockUserDao.findByUsername(user3.getUsername())).thenReturn(Optional.ofNullable(user3));
        when(mockTraineeDaoImpl.findByUser(user3)).thenReturn(Optional.ofNullable(trainee3));
        assertEquals(response, traineeService.selectByUsername(user3.getUsername()));
    }
    @Test
    void changePassword() {
        ChangeLoginRequest request
                = reader.readDto("trainees/trainee4", Trainee.class, traineeMapper::traineeToChangeLoginRequest);
        request.setNewPassword("newPassword");
        User userNew = reader.readEntity("users/user5", User.class);
        userNew.setPassword("newPassword");
        when(mockUserDao.findByUsername(request.getUsername())).thenReturn(Optional.ofNullable(user5));
        when(mockTraineeDaoImpl.findByUser(user5)).thenReturn(Optional.ofNullable(trainee5));
        when(mockUserDao.update(any(Integer.class), any(User.class))).thenReturn(Optional.of(userNew));
        assertTrue(traineeService.changePassword(request));
    }

    @Test
    void setActive() {
        ActivateDeactivateRequest request =
                reader.readDto("trainees/trainee4", Trainee.class, traineeMapper::traineeToActivateDeactivateRequest);
        request.setActive(false);
        user4.setActive(false);
        when(mockUserDao.findByUsername(request.getUsername())).thenReturn(Optional.ofNullable(user4));
        when(mockTraineeDaoImpl.findByUser(user4)).thenReturn(Optional.ofNullable(trainee4));
        assertTrue(traineeService.setActive(request));
    }

    @Test
    void delete() {
        when(mockUserDao.findByUsername(user3.getUsername())).thenReturn(Optional.ofNullable(user3));
        when(mockUserDao.delete(user3.getId())).thenReturn(Optional.ofNullable(user3));
        when(mockTraineeDaoImpl.findByUser(user3)).thenReturn(Optional.ofNullable(trainee3));
        assertTrue(traineeService.delete(user3.getUsername()));
        verify(mockUserDao).delete(user3.getId());
    }

    @Test
    void testGetTraineeTrainingsList() {
        Trainee trainee = reader.readEntity("trainees/trainee3", Trainee.class);
        Training training1 = reader.readEntity("trainings/training1", Training.class);
        Training training2 = reader.readEntity("trainings/training2", Training.class);
        List<Training> trainings = new ArrayList<>();
        trainings.add(training1);
        trainings.add(training2);
        trainee.setTrainings(trainings);
        User user = reader.readEntity("users/user3", User.class);
        GetTraineeTrainingsListRequest request = new GetTraineeTrainingsListRequest();
        request.setPeriodFrom(null);
        request.setPeriodTo(null);
        request.setTrainingType(null);
        GetTrainingsResponse response = new GetTrainingsResponse();
        response.setTrainings(trainingMapper.traineeTrainingsToShortDtos(trainings));
        when(mockUserDao.findByUsername(user3.getUsername())).thenReturn(Optional.ofNullable(user));
        when(mockTraineeDaoImpl.findByUser(user)).thenReturn(Optional.of(trainee));
        assertEquals(response, traineeService.getTraineeTrainingsList(user3.getUsername(), request));
    }
}

