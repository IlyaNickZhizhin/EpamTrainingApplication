package org.epam.service;


import org.epam.Reader;
import org.epam.dao.TrainerDaoImpl;
import org.epam.dao.UserDao;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.trainerDto.TrainerProfileResponse;
import org.epam.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.dto.trainerDto.UpdateTrainerProfileRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerDaoImpl mockTrainerDaoImpl = mock(TrainerDaoImpl.class);

    @Mock
    private UserDao mockUserDao = mock(UserDao.class);

    @Spy
    TraineeMapper traineeMapper = Mappers.getMapper(TraineeMapper.class);

    @Spy
    @InjectMocks
    TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);

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
        when(mockUserDao.setNewUser(user1.getFirstName(),user1.getLastName())).thenReturn(Optional.ofNullable(user1));
        when(mockTrainerDaoImpl.create(trainer)).thenReturn(Optional.ofNullable(trainer1));
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
        request.setFirstname("user1");
        TrainerProfileResponse response
                = trainerMapper.trainerToProfileResponse(trainer1);
        response.setFirstName("user1");
        when(mockUserDao.getByUsername(request.getUsername())).thenReturn(Optional.ofNullable(user1));
        when(mockTrainerDaoImpl.getModelByUser(user1)).thenReturn(Optional.ofNullable(trainer1));
        when(mockUserDao.update(1, user)).thenReturn(Optional.of(user));
        when(mockTrainerDaoImpl.update(1, trainer)).thenReturn(Optional.of(trainer));
        assertEquals(response, trainerService.update(request));
    }
    @Test
    void selectByUsername() {
        TrainerProfileResponse response = trainerMapper.trainerToProfileResponse(trainer1);
        when(mockUserDao.getByUsername(user1.getUsername())).thenReturn(Optional.ofNullable(user1));
        when(mockTrainerDaoImpl.getModelByUser(user1)).thenReturn(Optional.ofNullable(trainer1));
        assertEquals(response, trainerService.selectByUsername(user1.getUsername()));
    }
    @Test
    void changePassword() {
        ChangeLoginRequest request
                = reader.readDto("trainers/trainer2", Trainer.class, trainerMapper::trainerToChangeLoginRequest);
        request.setNewPassword("newPassword");
        User userNew = reader.readEntity("users/user2", User.class);
        userNew.setPassword("newPassword");
        when(mockUserDao.getByUsername(request.getUsername())).thenReturn(Optional.ofNullable(user2));
        when(mockTrainerDaoImpl.getModelByUser(user2)).thenReturn(Optional.ofNullable(trainer2));
        when(mockUserDao.update(any(Integer.class), any(User.class))).thenReturn(Optional.of(userNew));
        assertTrue(trainerService.changePassword(request));
    }
    @Test
    void setActive() {
        ActivateDeactivateRequest request =
                reader.readDto("trainers/trainer2", Trainer.class, trainerMapper::trainerToActivateDeactivateRequest);
        request.setActive(false);
        user2.setActive(false);
        when(mockUserDao.getByUsername(request.getUsername())).thenReturn(Optional.ofNullable(user2));
        when(mockTrainerDaoImpl.getModelByUser(user2)).thenReturn(Optional.ofNullable(trainer2));
        assertTrue(trainerService.setActive(request));
    }
}
