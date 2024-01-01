package org.epam.mainservice.service;


import org.epam.mainservice.Reader;
import org.epam.mainservice.dto.ActivateDeactivateRequest;
import org.epam.mainservice.dto.ChangeLoginRequest;
import org.epam.mainservice.dto.RegistrationResponse;
import org.epam.mainservice.dto.trainerDto.TrainerProfileResponse;
import org.epam.mainservice.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.mainservice.dto.trainerDto.UpdateTrainerProfileRequest;
import org.epam.mainservice.dto.trainingDto.GetTrainerTrainingsListRequest;
import org.epam.mainservice.dto.trainingDto.GetTrainingsResponse;
import org.epam.mainservice.mapper.TraineeMapper;
import org.epam.mainservice.mapper.TrainerMapper;
import org.epam.mainservice.mapper.TrainingMapper;
import org.epam.mainservice.model.Role;
import org.epam.mainservice.model.User;
import org.epam.mainservice.model.gymModel.Trainer;
import org.epam.mainservice.model.gymModel.Training;
import org.epam.mainservice.repository.TrainerRepository;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepository mockTrainerDaoImpl = mock(TrainerRepository.class);

    @Mock
    private UserService mockUserDao = mock(UserService.class);

    @Spy
    TraineeMapper traineeMapper = Mappers.getMapper(TraineeMapper.class);

    @Spy
    TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);

    @Spy
    PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Spy
    @InjectMocks
    TrainingMapper trainingMapper = Mappers.getMapper(TrainingMapper.class);

    @InjectMocks
    private TrainerService trainerService;

    Reader reader = new Reader();
    User user1; User user2;
    Trainer trainer1; Trainer trainer2;

    @BeforeEach
    public void setUp() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        trainer1 = reader.readEntity("trainers/trainer1", Trainer.class);
        trainer2 = reader.readEntity("trainers/trainer2", Trainer.class);
        user1 = reader.readEntity("users/user1", User.class);
        user2 = reader.readEntity("users/user2", User.class);
    }

    @Test
    void testCreate() {
        TrainerRegistrationRequest request =
                reader.readDto("trainers/trainer1", Trainer.class,trainerMapper::trainerToRegistrationRequest);
        RegistrationResponse response
                = reader.readDto("users/user1", User.class, traineeMapper::userToRegistrationResponce);
        Trainer trainer = trainer1;
        trainer.setId(0);
        when(mockUserDao.setNewUser(user1.getFirstName(),user1.getLastName(), Role.of(Role.Authority.ROLE_TRAINER))).thenReturn(Optional.ofNullable(user1));
        when(mockTrainerDaoImpl.save(trainer)).thenReturn(trainer1);
        assertEquals(response, trainerService.create(request));
    }

    @Test
    void testUpdate() {
        Trainer trainer = reader.readEntity("trainers/trainer1", Trainer.class);
        User user = reader.readEntity("users/user1", User.class);
        user.setFirstName("user1");
        trainer.getUser().setFirstName("user1");
        UpdateTrainerProfileRequest request
                = trainerMapper.trainerToUpdateRequest(trainer1);
        request.setUsername("user1");
        TrainerProfileResponse response
                = trainerMapper.trainerToProfileResponse(trainer1);
        response.setFirstName("user1");
        when(mockUserDao.findByUsername("user1")).thenReturn(Optional.ofNullable(user1));
        when(mockTrainerDaoImpl.findByUser(user1)).thenReturn(Optional.ofNullable(trainer1));
        when(mockUserDao.update(anyInt(), any(User.class))).thenReturn(Optional.of(user));
        when(mockTrainerDaoImpl.save(any(Trainer.class))).thenReturn(trainer);
        assertEquals(response, trainerService.update(request));
    }
    @Test
    void selectByUsername() {
        TrainerProfileResponse response = trainerMapper.trainerToProfileResponse(trainer1);
        when(mockUserDao.findByUsername(user1.getUsername())).thenReturn(Optional.ofNullable(user1));
        when(mockTrainerDaoImpl.findByUser(user1)).thenReturn(Optional.ofNullable(trainer1));
        assertEquals(response, trainerService.selectByUsername(user1.getUsername()));
    }
    @Test
    void changePassword() {
        ChangeLoginRequest request
                = reader.readDto("trainers/trainer2", Trainer.class, trainerMapper::trainerToChangeLoginRequest);
        request.setNewPassword("newPassword");
        User userNew = reader.readEntity("users/user2", User.class);
        String s = encoder.encode("newPassword");
        userNew.setPassword(s);
        when(mockUserDao.findByUsername(request.getUsername())).thenReturn(Optional.ofNullable(user2));
        when(mockTrainerDaoImpl.findByUser(user2)).thenReturn(Optional.ofNullable(trainer2));
        when(mockUserDao.update(any(Integer.class), any(User.class))).thenReturn(Optional.of(userNew));
        assertTrue(trainerService.changePassword(request));
    }
    @Test
    void setActive() {
        ActivateDeactivateRequest request =
                reader.readDto("trainers/trainer2", Trainer.class, trainerMapper::trainerToActivateDeactivateRequest);
        request.setActive(false);
        user2.setActive(false);
        when(mockUserDao.findByUsername(request.getUsername())).thenReturn(Optional.ofNullable(user2));
        when(mockTrainerDaoImpl.findByUser(user2)).thenReturn(Optional.ofNullable(trainer2));
        assertTrue(trainerService.setActive(request));
    }
    @Test
    void testGetTrainerTrainingsList() {
        Trainer trainer = reader.readEntity("trainers/trainer1", Trainer.class);
        Training training1 = reader.readEntity("trainings/training1", Training.class);
        Training training2 = reader.readEntity("trainings/training2", Training.class);
        List<Training> trainings = new ArrayList<>();
        trainings.add(training1);
        trainings.add(training2);
        trainer.setTrainings(trainings);
        User user = reader.readEntity("users/user1", User.class);
        GetTrainerTrainingsListRequest request = new GetTrainerTrainingsListRequest();
        request.setPeriodFrom(LocalDate.MIN);
        request.setPeriodTo(LocalDate.MAX);
        GetTrainingsResponse response = new GetTrainingsResponse();
        response.setTrainings(trainingMapper.trainerTrainingsToShortDtos(trainings));
        when(mockUserDao.findByUsername(user1.getUsername())).thenReturn(Optional.ofNullable(user));
        when(mockTrainerDaoImpl.findByUser(user)).thenReturn(Optional.of(trainer));
        assertEquals(response, trainerService.getTrainerTrainingsList(user1.getUsername(), request));
    }
}
