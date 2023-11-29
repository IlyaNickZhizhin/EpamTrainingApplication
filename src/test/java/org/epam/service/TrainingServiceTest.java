package org.epam.service;

import org.epam.Reader;
import org.epam.TestMapper;
import org.epam.dao.TrainingDaoImpl;
import org.epam.dao.UserDaoImpl;
import org.epam.dto.trainingDto.AddTrainingRequest;
import org.epam.dto.trainingDto.GetTraineeTrainingsListRequest;
import org.epam.dto.trainingDto.GetTrainerTrainingsListRequest;
import org.epam.dto.trainingDto.GetTrainersResponse;
import org.epam.dto.trainingDto.GetTrainingTypesResponse;
import org.epam.dto.trainingDto.GetTrainingsResponse;
import org.epam.dto.trainingDto.UpdateTraineeTrainerListRequest;
import org.epam.mapper.TrainingMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class TrainingServiceTest {

    @Mock
    private TrainingDaoImpl mockTrainingDaoImpl = mock(TrainingDaoImpl.class);

    @Mock
    private UserDaoImpl mockUserDao = mock(UserDaoImpl.class);

    @InjectMocks
    private TrainingService trainingService;

    private final static Reader reader = new Reader();
    TestMapper testMapper = TestMapper.INSTANCE;
    User user1; User user2; User user3; User user4; User user5; User user6;
    Trainer trainer1; Trainer trainer2;
    Trainee trainee3; Trainee trainee4; Trainee trainee5; Trainee trainee6;
    Training training1; Training training2; Training training3; Training training4;


    @BeforeEach
    void setUp() {
        initMocks(this);
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
        user1.setRole(trainer1);
        user2.setRole(trainer2);
        user3.setRole(trainee3);
        user4.setRole(trainee4);
        user5.setRole(trainee5);
        user6.setRole(trainee6);
        training1 = reader.readEntity("trainings/training1", Training.class);
        training2 = reader.readEntity("trainings/training2", Training.class);
        training3 = reader.readEntity("trainings/training3", Training.class);
        training4 = reader.readEntity("trainings/training4", Training.class);
    }

    @Test
    void testSelectAllTrainingTypes() {
        List<TrainingType> types = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            types.add(reader.readEntity("trainingtypes/trainingType" + i, TrainingType.class));
        }
        GetTrainingTypesResponse response = new GetTrainingTypesResponse();
        response.setTrainingTypes(types);
        when(mockTrainingDaoImpl.getAllTrainingTypes()).thenReturn(types);
        assertEquals(response, trainingService.selectAllTrainingTypes());
    }

    @Test
    void testCreate() {
        Training training = reader.readEntity("trainings/training1", Training.class);
        training.setId(0);
        AddTrainingRequest request = reader.readDto("trainings/training1", Training.class, testMapper::trainingToAddTrainingRequest);
        when(mockUserDao.getByUsername(request.getTraineeUsername())).thenReturn(user3);
        when(mockUserDao.getByUsername(request.getTrainerUsername())).thenReturn(user1);
        when(mockTrainingDaoImpl.create(training)).thenReturn(training1);
        assertEquals(request, trainingService.create(request));
    }

    @Test
    void testUpdateTrainersList() throws InstantiationException, IllegalAccessException {
        User user = reader.readEntity("users/user3", User.class);
        Trainee trainee = reader.readEntity("trainees/trainee3", Trainee.class);
        user.setRole(trainee);
        List<Trainer> trainerList = new ArrayList<>();
        trainerList.add(trainer1);
        trainerList.add(trainer2);
        trainee.setTrainers(trainerList);
        UpdateTraineeTrainerListRequest request
                = reader.readDto("trainees/trainee3", Trainee.class, testMapper::traineeToUpdateTrainerListRequest);
        request.setTrainerUsernames(trainerList.stream().map(testMapper::trainerToUsername).collect(Collectors.toList()));
        GetTrainersResponse response = TrainingMapper.INSTANCE.traineeToTrainersResponse(trainee);
        when(mockUserDao.getByUsername(request.getTraineeUsername())).thenReturn(user3);
        when(mockUserDao.getByUsername(request.getTrainerUsernames().get(0))).thenReturn(user1);
        when(mockUserDao.getByUsername(request.getTrainerUsernames().get(1))).thenReturn(user2);
        when(mockUserDao.update(anyInt(), any(User.class))).thenReturn(user);
        assertEquals(response, trainingService.updateTrainersList(request));
    }

    @Test
    void testGetTrainersList() {
        User user = reader.readEntity("users/user3", User.class);
        Trainee trainee = reader.readEntity("trainees/trainee3", Trainee.class);
        user.setRole(trainee);
        List<Trainer> trainerList = new ArrayList<>();
        trainerList.add(trainer1);
        trainerList.add(trainer2);
        trainee.setTrainers(trainerList);
        GetTrainersResponse response = TrainingMapper.INSTANCE.traineeToTrainersResponse(trainee);
        when(mockUserDao.getByUsername(user.getUsername())).thenReturn(user);
        assertEquals(response, trainingService.getTrainersList(user.getUsername()));
    }

    @Test
    public void testGetNotAssignedOnTraineeActiveTrainers() {
        Trainee trainee = reader.readEntity("trainees/trainee3", Trainee.class);
        List<Trainer> trainerList = new ArrayList<>();
        trainerList.add(trainer1);
        trainerList.add(trainer2);
        trainee.setTrainers(trainerList);
        GetTrainersResponse response = TrainingMapper.INSTANCE.traineeToTrainersResponse(trainee);
        when(mockUserDao.getByUsername(user3.getUsername())).thenReturn(user3);
        when(mockTrainingDaoImpl.getAllTrainersAvalibleForTrainee(any(Trainee.class), anyList())).thenReturn(trainerList);
        assertEquals(response, trainingService.getNotAssignedOnTraineeActiveTrainers(user3.getUsername()));
    }

    @Test
    void testGetTraineeTrainingsList() {
        Trainee trainee = reader.readEntity("trainees/trainee3", Trainee.class);
        List<Training> trainings = new ArrayList<>();
        trainings.add(training1);
        trainings.add(training2);
        trainee.setTrainings(trainings);
        User user = reader.readEntity("users/user3", User.class);
        user.setRole(trainee);
        GetTraineeTrainingsListRequest request = new GetTraineeTrainingsListRequest();
        request.setUsername(user3.getUsername());
        request.setPeriodFrom(null);
        request.setPeriodTo(null);
        request.setTrainingType(null);
        GetTrainingsResponse response = new GetTrainingsResponse();
        response.setTrainings(TrainingMapper.INSTANCE.traineeTrainingsToShortDtos(trainings));
        when(mockUserDao.getByUsername(user3.getUsername())).thenReturn(user);
        when(mockTrainingDaoImpl.getAllByUsernameAndTrainingTypes(anyList(), eq(trainee))).thenReturn(trainings);
        assertEquals(response, trainingService.getTraineeTrainingsList(request));
    }

    @Test
    void testGetTrainerTrainingsList() {
        Trainer trainer = reader.readEntity("trainers/trainer1", Trainer.class);
        List<Training> trainings = new ArrayList<>();
        trainings.add(training1);
        trainings.add(training2);
        trainer.setTrainings(trainings);
        User user = reader.readEntity("users/user1", User.class);
        user.setRole(trainer);
        GetTrainerTrainingsListRequest request = new GetTrainerTrainingsListRequest();
        request.setUsername(user1.getUsername());
        request.setPeriodFrom(LocalDate.MIN);
        request.setPeriodTo(LocalDate.MAX);
        GetTrainingsResponse response = new GetTrainingsResponse();
        response.setTrainings(TrainingMapper.INSTANCE.trainerTrainingsToShortDtos(trainings));
        when(mockUserDao.getByUsername(user1.getUsername())).thenReturn(user);
        when(mockTrainingDaoImpl.getAllByUsernameAndTrainingTypes(anyList(), eq(trainer))).thenReturn(trainings);
        assertEquals(response, trainingService.getTrainerTrainingsList(request));
    }
}