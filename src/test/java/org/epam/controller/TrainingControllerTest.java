package org.epam.controller;

import org.epam.Reader;
import org.epam.dto.trainingDto.*;
import org.epam.exceptions.InvalidDataException;
import org.epam.mapper.TrainerMapper;
import org.epam.mapper.TrainingMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.epam.service.TrainingService;
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
class TrainingControllerTest {

    @Mock
    TrainingService trainingService;

    @InjectMocks
    TrainingController trainingController;
    @Spy
    TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);
    @Spy
    @InjectMocks
    TrainingMapper trainingMapper = Mappers.getMapper(TrainingMapper.class);


    private final static Reader reader = new Reader();
    User user1; User user2; User user3; User user4; User user5; User user6;
    Trainer trainer1; Trainer trainer2;
    Trainee trainee3; Trainee trainee4; Trainee trainee5; Trainee trainee6;
    Training training1; Training training2; Training training3; Training training4;


    @BeforeEach
    void setUp() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        trainer1 = reader.readEntity("trainers/trainer1", Trainer.class);
        trainer2 = reader.readEntity("trainers/trainer2", Trainer.class);
        trainee3 = reader.readEntity("trainees/trainee3", Trainee.class);
        trainee4 = reader.readEntity("trainees/trainee4", Trainee.class);
        trainee5 = reader.readEntity("trainees/trainee5", Trainee.class);
        trainee6 = reader.readEntity("trainees/trainee6", Trainee.class);
        user1 = reader.readEntity("users/user1", User.class);
        user2 = reader.readEntity("users/user2", User.class);
        user3 = reader.readEntity("users/user3", User.class);
        user4 = reader.readEntity("users/user4", User.class);
        user5 = reader.readEntity("users/user5", User.class);
        user6 = reader.readEntity("users/user6", User.class);
        training1 = reader.readEntity("trainings/training1", Training.class);
        training2 = reader.readEntity("trainings/training2", Training.class);
        training3 = reader.readEntity("trainings/training3", Training.class);
        training4 = reader.readEntity("trainings/training4", Training.class);
    }

    @Test
    void testTrainingCreate() {
        AddTrainingRequest request = trainingMapper.trainingToAddTrainingRequest(training1);
        when(trainingService.create(request)).thenReturn(request);
        assertEquals(request, trainingController.create(request).getBody());
    }

    @Test
    void testGetAllTrainingTypes() {
        List<TrainingType> types = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            types.add(reader.readEntity("trainingtypes/trainingType" + i, TrainingType.class));
        }
        GetTrainingTypesResponse response = new GetTrainingTypesResponse();
        response.setTrainingTypes(types);
        when(trainingService.selectAllTrainingTypes()).thenReturn(response);
        assertEquals(response, trainingController.getAllTrainingTypes().getBody());
    }

    @Test
    void testUpdateTrainersList() {
        List<Trainer> trainerList = new ArrayList<>();
        trainerList.add(trainer1);
        trainerList.add(trainer2);
        trainee3.setTrainers(trainerList);
        GetTrainersResponse response = trainingMapper.traineeToTrainersResponse(trainee3);
        UpdateTraineeTrainerListRequest request = trainingMapper.traineeToUpdateTrainerListRequest(trainee3);
        when(trainingService.updateTrainersList(request)).thenReturn(response);
        assertEquals(response, trainingController.updateTrainersList(request).getBody());
    }

    @Test
    void testGetTrainersList() {
        List<Trainer> trainerList = new ArrayList<>();
        trainerList.add(trainer1);
        trainerList.add(trainer2);
        trainee3.setTrainers(trainerList);
        GetTrainersResponse response = trainingMapper.traineeToTrainersResponse(trainee3);
        when(trainingService.getTrainersList(trainee3.getUser().getUsername())).thenReturn(response);
        assertEquals(response, trainingController.getTrainersList(trainee3.getUser().getUsername()).getBody());
    }

    @Test
    void testGetNotAssignedOnTraineeActiveTrainers() {
        List<Trainer> trainerList = new ArrayList<>();
        trainerList.add(trainer1);
        trainerList.add(trainer2);
        trainee3.setTrainers(trainerList);
        GetTrainersResponse response = trainingMapper.traineeToTrainersResponse(trainee3);
        when(trainingService.getNotAssignedOnTraineeActiveTrainers(trainee3.getUser().getUsername())).thenReturn(response);
        assertEquals(response, trainingController.getNotAssignedOnTraineeActiveTrainers(trainee3.getUser().getUsername()).getBody());
    }

    @Test
    void testGetTraineeTrainingsList() {
        List<Training> trainingList = new ArrayList<>();
        trainingList.add(training1);
        trainingList.add(training2);
        trainee3.setTrainings(trainingList);
        GetTraineeTrainingsListRequest request = new GetTraineeTrainingsListRequest();
        GetTrainingsResponse response = new GetTrainingsResponse();
        response.setTrainings(trainingMapper.traineeTrainingsToShortDtos(trainee3.getTrainings()));
        when(trainingService.getTraineeTrainingsList(user3.getUsername(), request)).thenReturn(response);
        assertEquals(response, trainingController.getTraineeTrainingsList(user3.getUsername(), null, null, null, null).getBody());
    }

    @Test
    void testGetTrainerTrainingsList() {
        List<Training> trainings = new ArrayList<>();
        trainings.add(training1);
        trainings.add(training2);
        trainer1.setTrainings(trainings);
        GetTrainerTrainingsListRequest request = new GetTrainerTrainingsListRequest();
        GetTrainingsResponse response = new GetTrainingsResponse();
        response.setTrainings(trainingMapper.trainerTrainingsToShortDtos(trainings));
        when(trainingService.getTrainerTrainingsList(user1.getUsername(), request)).thenReturn(response);
        assertEquals(response, trainingController.getTrainerTrainingsList(user1.getUsername(), LocalDate.MIN, LocalDate.MAX, null).getBody());
    }

    @Test
    void testTrainingCreateEx() {
        AddTrainingRequest request = trainingMapper.trainingToAddTrainingRequest(training1);
        InvalidDataException ex = new InvalidDataException("1","2");
        when(trainingService.create(request)).thenThrow(ex);
        assertEquals(new ResponseEntity<>(
                new AddTrainingRequest(), HttpStatus.BAD_REQUEST),
                trainingController.create(request));
    }

    @Test
    void testUpdateTrainersListEx() {
        UpdateTraineeTrainerListRequest request = trainingMapper.traineeToUpdateTrainerListRequest(trainee3);
        when(trainingService.updateTrainersList(request)).thenThrow(new InvalidDataException("1","2"));
        assertEquals(new ResponseEntity<>(
                new GetTrainersResponse(), HttpStatus.BAD_REQUEST),
                trainingController.updateTrainersList(request));
    }

    @Test
    void testGetTrainersListEx() {
        when(trainingService.getTrainersList(trainee3.getUser().getUsername()))
                .thenThrow(new InvalidDataException("1","2"));
        assertEquals(new ResponseEntity<>(
                new GetTrainersResponse(), HttpStatus.BAD_REQUEST),
                trainingController.getTrainersList(trainee3.getUser().getUsername()));
    }

    @Test
    void testGetNotAssignedOnTraineeActiveTrainersEx() {
        when(trainingService
                .getNotAssignedOnTraineeActiveTrainers(trainee3.getUser().getUsername()))
                .thenThrow(new InvalidDataException("1","2"));
        assertEquals(new ResponseEntity<>(
                new GetTrainersResponse(), HttpStatus.BAD_REQUEST),
                trainingController.getNotAssignedOnTraineeActiveTrainers(trainee3.getUser().getUsername()));
    }

    @Test
    void testGetTraineeTrainingsListEx() {
        GetTraineeTrainingsListRequest request = new GetTraineeTrainingsListRequest();
        request.setPeriodFrom(null);
        request.setPeriodTo(null);
        request.setTrainingType(null);
        when(trainingService.getTraineeTrainingsList(user1.getUsername(), request))
                .thenThrow(new InvalidDataException("1","2"));
        assertEquals(
                new ResponseEntity<>(new GetTrainingsResponse(), HttpStatus.BAD_REQUEST),
                trainingController.getTraineeTrainingsList(user1.getUsername(), null, null, null, null));
    }

    @Test
    void testGetTrainerTrainingsListEx() {
        GetTrainerTrainingsListRequest request = new GetTrainerTrainingsListRequest();
        when(trainingService.getTrainerTrainingsList(user1.getUsername(), request))
                .thenThrow(new InvalidDataException("1","2"));
        assertEquals(
                new ResponseEntity<>(new GetTrainingsResponse(), HttpStatus.BAD_REQUEST),
                trainingController.getTrainerTrainingsList(user1.getUsername(), LocalDate.MIN, LocalDate.MAX, null));
    }

}