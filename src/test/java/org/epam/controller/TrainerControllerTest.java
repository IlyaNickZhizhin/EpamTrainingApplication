package org.epam.controller;

import org.epam.Reader;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.trainerDto.TrainerProfileResponse;
import org.epam.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.dto.trainerDto.UpdateTrainerProfileRequest;
import org.epam.dto.trainingDto.GetTrainerTrainingsListRequest;
import org.epam.dto.trainingDto.GetTrainingsResponse;
import org.epam.exceptions.InvalidDataException;
import org.epam.mapper.TraineeMapper;
import org.epam.mapper.TrainerMapper;
import org.epam.mapper.TrainingMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.service.TrainerService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);

    @Spy
    @InjectMocks
    TrainingMapper trainingMapper = Mappers.getMapper(TrainingMapper.class);

    Reader reader = new Reader();
    Trainer trainer1; Trainer trainer2; User user1; User user2;

    @BeforeEach
    public void setUp() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        user1 = reader.readEntity("users/user1", User.class);
        user2 = reader.readEntity("users/user2", User.class);
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
        TrainerProfileResponse response = trainerMapper.trainerToProfileResponse(trainer1);
        when(trainerService.update(request)).thenReturn(response);
        assertEquals(response, trainerController.update(request).getBody());
    }

    @Test
    void testTrainerUpdateEx() {
        UpdateTrainerProfileRequest request = trainerMapper.trainerToUpdateRequest(trainer1);
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

    @Test
    void testGetTrainerTrainingsList() {
        List<Training> trainings = new ArrayList<>();
        Training training1 = reader.readEntity("trainings/training1", Training.class);
        Training training2 = reader.readEntity("trainings/training2", Training.class);
        trainings.add(training1);
        trainings.add(training2);
        trainer1.setTrainings(trainings);
        GetTrainerTrainingsListRequest request = new GetTrainerTrainingsListRequest();
        request.setPeriodFrom(LocalDate.MIN);
        request.setPeriodTo(LocalDate.MAX);
        GetTrainingsResponse response = new GetTrainingsResponse();
        response.setTrainings(trainingMapper.trainerTrainingsToShortDtos(trainings));
        when(trainerService.getTrainerTrainingsList(user1.getUsername(), request)).thenReturn(response);
        assertEquals(response, trainerController.getTrainerTrainingsList(user1.getUsername(), LocalDate.MIN, LocalDate.MAX, null).getBody());
    }
    @Test
    void testGetTrainerTrainingsListEx() {
        GetTrainerTrainingsListRequest request = new GetTrainerTrainingsListRequest();
        request.setPeriodFrom(LocalDate.MIN);
        request.setPeriodTo(LocalDate.MAX);
        when(trainerService.getTrainerTrainingsList(user1.getUsername(), request))
                .thenThrow(new InvalidDataException("1","2"));
        assertEquals(
                new ResponseEntity<>(new GetTrainingsResponse(), HttpStatus.BAD_REQUEST),
                trainerController.getTrainerTrainingsList(user1.getUsername(), LocalDate.MIN, LocalDate.MAX, null));
    }

}