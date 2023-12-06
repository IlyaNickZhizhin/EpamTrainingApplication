package org.epam.controller;

import org.epam.Reader;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.trainerDto.TrainerProfileResponse;
import org.epam.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.dto.trainerDto.UpdateTrainerProfileRequest;
import org.epam.exceptions.InvalidDataException;
import org.epam.mapper.TraineeMapper;
import org.epam.mapper.TrainerMapper;
import org.epam.model.gymModel.Trainer;
import org.epam.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerController trainerController;

    @Spy
    TraineeMapper traineeMapper = Mappers.getMapper(TraineeMapper.class);
    @Spy
    @InjectMocks
    TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);

    Reader reader = new Reader();
    Trainer trainer1; Trainer trainer2;

    @BeforeEach
    public void setUp() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        trainer1 = reader.readEntity("trainers/trainer1", Trainer.class);
        trainer2 = reader.readEntity("trainers/trainer2", Trainer.class);
    }


    @Test
    void testTrainerRegister() {
        TrainerRegistrationRequest request = trainerMapper.trainerToRegistrationRequest(trainer1);
        RegistrationResponse response = trainerMapper.trainerToRegistrationResponse(trainer1);
        when(trainerService.create(request)).thenReturn(response);
        assertEquals(response, trainerController.register(request).getBody());
    }

    @Test
    void testTrainerRegisterEx() {
        TrainerRegistrationRequest request = trainerMapper.trainerToRegistrationRequest(trainer1);
        RegistrationResponse response = new RegistrationResponse();
        InvalidDataException ex = new InvalidDataException("1","2");
        when(trainerService.create(request)).thenThrow(ex);
        assertEquals(response, trainerController.register(request).getBody());
    }

    @Test
    void testTrainerChangePassword() {
        ChangeLoginRequest request = trainerMapper.trainerToChangeLoginRequest(trainer1);
        when(trainerService.changePassword(request)).thenReturn(true);
        assertEquals(true, trainerController.changePassword(request).getBody());
    }

    @Test
    void testTrainerChangePasswordEx() {
        ChangeLoginRequest request = trainerMapper.trainerToChangeLoginRequest(trainer1);
        when(trainerService.changePassword(request)).thenThrow(new InvalidDataException("1","2"));
        assertEquals(false, trainerController.changePassword(request).getBody());
    }

    @Test
    void testTrainerSetActive() {
        ActivateDeactivateRequest request = trainerMapper.trainerToActivateDeactivateRequest(trainer1);
        when(trainerService.setActive(request)).thenReturn(true);
        assertEquals(true, trainerController.setActive(request).getBody());
    }

    @Test
    void testTrainerSetActiveEx() {
        ActivateDeactivateRequest request = trainerMapper.trainerToActivateDeactivateRequest(trainer1);
        when(trainerService.setActive(request)).thenThrow(new InvalidDataException("1","2"));
        assertEquals(false, trainerController.setActive(request).getBody());
    }

    @Test
    void testTrainerUpdate() {
        UpdateTrainerProfileRequest request = trainerMapper.trainerToUpdateRequest(trainer1);
        request.setFirstname("User1");
        TrainerProfileResponse response = trainerMapper.trainerToProfileResponse(trainer1);
        response.setFirstName("User1");
        when(trainerService.update(request)).thenReturn(response);
        assertEquals(response, trainerController.update(request).getBody());
    }

    @Test
    void testTrainerUpdateEx() {
        UpdateTrainerProfileRequest request = trainerMapper.trainerToUpdateRequest(trainer1);
        request.setFirstname("User1");
        TrainerProfileResponse response = new TrainerProfileResponse();
        when(trainerService.update(request)).thenThrow(new InvalidDataException("1","2"));
        assertEquals(response, trainerController.update(request).getBody());
    }

    @Test
    void testTrainerSelectByUsername() {
        TrainerProfileResponse response = trainerMapper.trainerToProfileResponse(trainer2);
        when(trainerService.selectByUsername(trainer2.getUser().getUsername())).thenReturn(response);
        assertEquals(response, trainerController.selectByUsername(trainer2.getUser().getUsername()).getBody());
    }

    @Test
    void testTrainerSelectByUsernameEx() {
        TrainerProfileResponse response = new TrainerProfileResponse();
        when(trainerService.selectByUsername(trainer2.getUser().getUsername())).thenThrow(new InvalidDataException("1","2"));
        assertEquals(response, trainerController.selectByUsername(trainer2.getUser().getUsername()).getBody());
    }

}