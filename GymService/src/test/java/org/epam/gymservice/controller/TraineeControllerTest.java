package org.epam.gymservice.controller;

import org.epam.gymservice.Reader;
import org.epam.gymservice.dto.ActivateDeactivateRequest;
import org.epam.gymservice.dto.ChangeLoginRequest;
import org.epam.gymservice.dto.RegistrationResponse;
import org.epam.gymservice.dto.traineeDto.TraineeProfileResponse;
import org.epam.gymservice.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.gymservice.dto.traineeDto.UpdateTraineeProfileRequest;
import org.epam.gymservice.dto.trainingDto.GetTraineeTrainingsListRequest;
import org.epam.gymservice.dto.trainingDto.GetTrainingsResponse;
import org.epam.gymservice.exceptions.InvalidDataException;
import org.epam.gymservice.mapper.TraineeMapper;
import org.epam.gymservice.mapper.TrainerMapper;
import org.epam.gymservice.mapper.TrainingMapper;
import org.epam.gymservice.model.User;
import org.epam.gymservice.model.gymModel.Trainee;
import org.epam.gymservice.model.gymModel.Training;
import org.epam.gymservice.service.TraineeService;
import org.epam.gymservice.service.feign.AsyncFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    AsyncFeignClient feignClient;

    @InjectMocks
    private TraineeController traineeController;

    @Spy
    TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);
    @Spy
    TraineeMapper traineeMapper = Mappers.getMapper(TraineeMapper.class);

    @Spy
    @InjectMocks
    TrainingMapper trainingMapper = Mappers.getMapper(TrainingMapper.class);

    Reader reader = new Reader();
    Trainee trainee3; Trainee trainee4; User user3; User user1;


    @BeforeEach
    public void setUp() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        user3 = reader.readEntity("users/user3", User.class);
        user1 = reader.readEntity("users/user1", User.class);
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
        TraineeProfileResponse response = traineeMapper.traineeToProfileResponse(trainee3);
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
        assertEquals(true, traineeController.delete("token", trainee3.getUser().getUsername()).getBody());
    }

    @Test
    void testTraineeDeleteEx() {
        when(traineeService.delete(trainee3.getUser().getUsername())).thenThrow(new InvalidDataException("1","2"));
        assertEquals(false, traineeController.delete("token", trainee3.getUser().getUsername()).getBody());
    }

    @Test
    void testGetTraineeTrainingsList() {
        List<Training> trainingList = new ArrayList<>();
        Training training1 = reader.readEntity("trainings/training1", Training.class);
        Training training2 = reader.readEntity("trainings/training2", Training.class);
        trainingList.add(training1);
        trainingList.add(training2);
        trainee3.setTrainings(trainingList);
        GetTraineeTrainingsListRequest request = new GetTraineeTrainingsListRequest();
        GetTrainingsResponse response = new GetTrainingsResponse();
        response.setTrainings(trainingMapper.traineeTrainingsToShortDtos(trainee3.getTrainings()));
        when(traineeService.getTraineeTrainingsList(user3.getUsername(), request)).thenReturn(response);
        assertEquals(response, traineeController.getTraineeTrainingsList(user3.getUsername(), null, null, null, null).getBody());
    }

    @Test
    void testGetTraineeTrainingsListEx() {
        GetTraineeTrainingsListRequest request = new GetTraineeTrainingsListRequest();
        request.setPeriodFrom(null);
        request.setPeriodTo(null);
        request.setTrainingType(null);
        when(traineeService.getTraineeTrainingsList(user1.getUsername(), request))
                .thenThrow(new InvalidDataException("1","2"));
        assertEquals(
                new ResponseEntity<>(new GetTrainingsResponse(), HttpStatus.BAD_REQUEST),
                traineeController.getTraineeTrainingsList(user1.getUsername(), null, null, null, null));
    }
}