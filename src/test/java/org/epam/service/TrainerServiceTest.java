package org.epam.service;


import org.epam.Reader;
import org.epam.TestMapper;
import org.epam.dao.TrainerDaoImpl;
import org.epam.dao.UserDaoImpl;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.trainerDto.TrainerProfileResponse;
import org.epam.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.dto.trainerDto.UpdateTrainerProfileRequest;
import org.epam.mapper.TrainerMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class TrainerServiceTest {

    @Mock
    private TrainerDaoImpl mockTrainerDaoImpl = mock(TrainerDaoImpl.class);

    @Mock
    private UserDaoImpl mockUserDao = mock(UserDaoImpl.class);

    @InjectMocks
    private TrainerService trainerService;

    TestMapper testMapper = TestMapper.INSTANCE;
    TrainerMapper trainerMapper = TrainerMapper.INSTANCE;

    Reader reader = new Reader();
    User user1; User user2;
    Trainer trainer1; Trainer trainer2;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        trainer1 = reader.readEntity("trainers/trainer1", Trainer.class);
        trainer2 = reader.readEntity("trainers/trainer2", Trainer.class);
        user1 = reader.readEntity("users/user1", User.class);
        user1.setRole(trainer1);
        user2 = reader.readEntity("users/user2", User.class);
        user2.setRole(trainer2);
        when(mockTrainerDaoImpl.getModelByUser(user1)).thenReturn(trainer1);
        when(mockTrainerDaoImpl.getModelByUser(user2)).thenReturn(trainer2);
        when(mockUserDao.getByUsername("user1")).thenReturn(user1);
        when(mockUserDao.getByUsername("user2")).thenReturn(user2);
    }

    @Test
    void testCreate() {
        TrainerRegistrationRequest request =
                reader.readDto("trainers/trainer1", Trainer.class,testMapper::trainerToRegistrationRequest);
        RegistrationResponse response
                = reader.readDto("users/user1", User.class, testMapper::userToRegistrationResponce);
        Trainer trainer = trainer1;
        trainer.setId(0);
        when(mockUserDao.setNewUser(user1.getFirstName(),user1.getLastName())).thenReturn(user1);
        when(mockTrainerDaoImpl.create(trainer)).thenReturn(trainer1);
        assertEquals(response, trainerService.create(request));
    }

    @Test
    void testUpdate() {
        Trainer trainer = reader.readEntity("trainers/trainer1", Trainer.class);
        trainer.getUser().setFirstName("user1");
        UpdateTrainerProfileRequest request
                = testMapper.trainerToUpdateRequest(trainer1);
        request.setFirstname("user1");
        TrainerProfileResponse response
                = trainerMapper.trainerToProfileResponse(trainer1);
        response.setFirstName("user1");
        when(mockUserDao.getByUsername(request.getUsername())).thenReturn(user1);
        when(mockTrainerDaoImpl.getModelByUser(user1)).thenReturn(trainer1);
        when(mockTrainerDaoImpl.update(1, trainer)).thenReturn(trainer);
        assertEquals(response, trainerService.update(request));
    }
    @Test
    void selectByUsername() {
        TrainerProfileResponse response = trainerMapper.trainerToProfileResponse(trainer1);
        when(mockUserDao.getByUsername(user1.getUsername())).thenReturn(user1);
        when(mockTrainerDaoImpl.getModelByUser(user1)).thenReturn(trainer1);
        assertEquals(response, trainerService.selectByUsername(user1.getUsername()));
    }
    @Test
    void changePassword() {
        ChangeLoginRequest request
                = reader.readDto("trainers/trainer2", Trainer.class, testMapper::roleToChangeLoginRequest);
        request.setNewPassword("newPassword");
        User userNew = reader.readEntity("users/user2", User.class);
        userNew.setPassword("newPassword");
        when(mockUserDao.getByUsername(request.getUsername())).thenReturn(user2);
        when(mockTrainerDaoImpl.getModelByUser(user2)).thenReturn(trainer2);
        when(mockUserDao.update(any(Integer.class), any(User.class))).thenReturn(userNew);
        assertTrue(trainerService.changePassword(request));
    }
    @Test
    void setActive() {
        ActivateDeactivateRequest request =
                reader.readDto("trainers/trainer2", Trainer.class, testMapper::roleToActivateDeactivateRequest);
        request.setActive(false);
        user2.setActive(false);
        when(mockUserDao.getByUsername(request.getUsername())).thenReturn(user2);
        when(mockTrainerDaoImpl.getModelByUser(user2)).thenReturn(trainer2);
        assertFalse(trainerService.setActive(request));
    }
}
