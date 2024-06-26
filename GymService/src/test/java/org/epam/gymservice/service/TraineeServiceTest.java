package org.epam.gymservice.service;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.epam.gymservice.Reader;
import org.epam.gymservice.dto.ActivateDeactivateRequest;
import org.epam.gymservice.dto.ChangeLoginRequest;
import org.epam.gymservice.dto.RegistrationResponse;
import org.epam.gymservice.dto.traineeDto.TraineeProfileResponse;
import org.epam.gymservice.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.gymservice.dto.traineeDto.UpdateTraineeProfileRequest;
import org.epam.gymservice.dto.trainingDto.GetTraineeTrainingsListRequest;
import org.epam.gymservice.dto.trainingDto.GetTrainingsResponse;
import org.epam.gymservice.mapper.TraineeMapper;
import org.epam.gymservice.mapper.TrainerMapper;
import org.epam.gymservice.mapper.TrainingMapper;
import org.epam.gymservice.model.Role;
import org.epam.gymservice.model.User;
import org.epam.gymservice.model.gymModel.Trainee;
import org.epam.gymservice.model.gymModel.Training;
import org.epam.gymservice.repository.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    @Spy
    PasswordEncoder encoder = new BCryptPasswordEncoder();
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
        user4.setPassword(encoder.encode(user4.getPassword()));
    }

    @Test
    void testCreate() {
        TraineeRegistrationRequest request =
                reader.readDto("trainees/trainee3", Trainee.class,traineeMapper::traineeToRegistrationRequest);
        RegistrationResponse response
                = reader.readDto("users/user3", User.class, traineeMapper::userToRegistrationResponce);
        Trainee trainee = trainee3;
        trainee.setId(0);
        when(mockUserDao.setNewUser(user3.getFirstName(),user3.getLastName(), Role.of(Role.Authority.TRAINEE)))
                .thenReturn(ImmutablePair.of(Optional.ofNullable(user3), Objects.requireNonNull(user3).getPassword()));
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
        assertEquals(response, traineeService.update(request));
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
        User userNew = reader.readEntity("users/user4", User.class);
        userNew.setPassword(encoder.encode("newPassword"));
        when(mockUserDao.findByUsername(request.getUsername())).thenReturn(Optional.ofNullable(user4));
        when(mockTraineeDaoImpl.findByUser(user4)).thenReturn(Optional.ofNullable(trainee4));
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

