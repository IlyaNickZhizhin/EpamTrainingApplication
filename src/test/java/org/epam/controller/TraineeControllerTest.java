package org.epam.controller;

import org.epam.Reader;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeProfileResponse;
import org.epam.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.dto.traineeDto.UpdateTraineeProfileRequest;
import org.epam.exceptions.InvalidDataException;
import org.epam.mapper.TraineeMapper;
import org.epam.mapper.TrainerMapper;
import org.epam.model.gymModel.Trainee;
import org.epam.service.TraineeService;
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
public class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;

    @InjectMocks
    private TraineeController traineeController;

    @Spy
    TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);
    @Spy
    @InjectMocks
    TraineeMapper traineeMapper = Mappers.getMapper(TraineeMapper.class);

    Reader reader = new Reader();
    Trainee trainee3; Trainee trainee4;


    @BeforeEach
    public void setUp() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        trainee3 = reader.readEntity("trainees/trainee3", Trainee.class);
        trainee4 = reader.readEntity("trainees/trainee4", Trainee.class);
    }

    @Test
    void testTraineeRegister() {
        TraineeRegistrationRequest request = traineeMapper.traineeToRegistrationRequest(trainee3);
        RegistrationResponse response = traineeMapper.traineeToRegistrationResponse(trainee3);
        when(traineeService.create(request)).thenReturn(response);
        assertEquals(response, traineeController.register(request).getBody());
    }

    @Test
    void testTraineeRegisterEx() {
        TraineeRegistrationRequest request = traineeMapper.traineeToRegistrationRequest(trainee3);
        RegistrationResponse response = new RegistrationResponse();
        InvalidDataException ex = new InvalidDataException("1","2");
        when(traineeService.create(request)).thenThrow(ex);
        assertEquals(response, traineeController.register(request).getBody());
    }

    @Test
    void testTraineeChangePassword() {
        ChangeLoginRequest request = traineeMapper.traineeToChangeLoginRequest(trainee3);
        when(traineeService.changePassword(request)).thenReturn(true);
        assertEquals(true, traineeController.changePassword(request).getBody());
    }

    @Test
    void testTraineeChangePasswordEx() {
        ChangeLoginRequest request = traineeMapper.traineeToChangeLoginRequest(trainee3);
        when(traineeService.changePassword(request)).thenThrow(new InvalidDataException("1","2"));
        assertEquals(false, traineeController.changePassword(request).getBody());
    }

    @Test
    void testTraineeSetActive() {
        ActivateDeactivateRequest request = traineeMapper.traineeToActivateDeactivateRequest(trainee3);
        when(traineeService.setActive(request)).thenReturn(true);
        assertEquals(true, traineeController.setActive(request).getBody());
    }

    @Test
    void testTraineeSetActiveEx() {
        ActivateDeactivateRequest request = traineeMapper.traineeToActivateDeactivateRequest(trainee3);
        when(traineeService.setActive(request)).thenThrow(new InvalidDataException("1","2"));
        assertEquals(false, traineeController.setActive(request).getBody());
    }

    @Test
    void testTraineeUpdate() {
        UpdateTraineeProfileRequest request = traineeMapper.traineeToUpdateRequest(trainee3);
        request.setFirstname("User3");
        TraineeProfileResponse response = traineeMapper.traineeToProfileResponse(trainee3);
        response.setFirstname("User3");
        when(traineeService.update(request)).thenReturn(response);
        assertEquals(response, traineeController.update(request).getBody());
    }

    @Test
    void testTraineeUpdateEx() {
        UpdateTraineeProfileRequest request = traineeMapper.traineeToUpdateRequest(trainee3);
        request.setFirstname("User3");
        TraineeProfileResponse response = new TraineeProfileResponse();
        when(traineeService.update(request)).thenThrow(new InvalidDataException("1","2"));
        assertEquals(response, traineeController.update(request).getBody());
    }

    @Test
    void testTraineeSelectByUsername() {
        TraineeProfileResponse response = traineeMapper.traineeToProfileResponse(trainee4);
        when(traineeService.selectByUsername(trainee4.getUser().getUsername())).thenReturn(response);
        assertEquals(response, traineeController.selectByUsername(trainee4.getUser().getUsername()).getBody());
    }

    @Test
    void testTraineeSelectByUsernameEx() {
        TraineeProfileResponse response = new TraineeProfileResponse();
        when(traineeService.selectByUsername(trainee4.getUser().getUsername())).thenThrow(new InvalidDataException("1","2"));
        assertEquals(response, traineeController.selectByUsername(trainee4.getUser().getUsername()).getBody());
    }

    @Test
    void testTraineeDelete() {
        when(traineeService.delete(trainee3.getUser().getUsername())).thenReturn(true);
        assertEquals(true, traineeController.delete(trainee3.getUser().getUsername()).getBody());
    }

    @Test
    void testTraineeDeleteEx() {
        when(traineeService.delete(trainee3.getUser().getUsername())).thenThrow(new InvalidDataException("1","2"));
        assertEquals(false, traineeController.delete(trainee3.getUser().getUsername()).getBody());
    }
}