package org.epam.service;

import org.epam.dao.TrainingDaoImpl;
import org.epam.dao.UserDaoImpl;
import org.epam.exceptions.ResourceNotFoundException;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.epam.TestDatabaseInitializer.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class TrainingServiceTest {

    @Mock
    private TrainingDaoImpl mockTrainingDaoImpl = mock(TrainingDaoImpl.class);

    @Mock
    private TrainerService mockTrainerService = mock(TrainerService.class);

    @Mock
    private TraineeService mockTraineeService = mock(TraineeService.class);

    @Mock
    private UserDaoImpl mockUserDao = mock(UserDaoImpl.class);

    @InjectMocks
    private TrainingService trainingService;
    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    public void testGetAllTrainersAvalibleForTrainee() {
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(trainer1);
        trainers.add(trainer2);
        when(mockUserDao.getByUsername(trainee3_Username)).thenReturn(user3);
        when(mockTrainerService.selectAll()).thenReturn(trainers);
        when(mockTrainingDaoImpl.getAllTrainersAvalibleForTrainee(trainee3, trainers)).thenReturn(List.of(trainer2));
        when(mockTraineeService.selectByUsername(trainee3_Username, trainee3_Password))
                .thenReturn(trainee3);
        assertEquals(List.of(trainer2), trainingService.getAllTrainersAvalibleForTrainee(trainee3_Username, trainee3_Password));
    }

    @Test
    public void testGetTrainingsByTRAINERusernameAndTrainingTypes() {
        when(mockUserDao.getByUsername(trainer2_Username)).thenReturn(user2);
        when(mockTrainingDaoImpl
                .getAllByUsernameAndTrainingTypes(trainer2_Username, List.of(trainingType2), trainer2))
                .thenReturn(List.of(training2));
        when(mockTraineeService.selectByUsername(trainer2_Username, trainer2_Password))
                .thenThrow(ResourceNotFoundException.class);
        when(mockTrainerService.selectByUsername(trainer2_Username, trainer2_Password))
                .thenReturn(trainer2);
        assertEquals(List.of(training2),
                trainingService
                        .getTrainingsByUsernameAndTrainingTypes(
                                trainer2_Username,
                                trainer2_Password,
                                List.of(trainingType2)));
    }

    @Test
    public void testGetTrainingsByTRAINEEusernameAndTrainingTypesForTrainee() {
        when(mockUserDao.getByUsername(trainee4_Username)).thenReturn(user4);
        when(mockTrainingDaoImpl
                .getAllByUsernameAndTrainingTypes(trainee4_Username, List.of(trainingType2), trainee4))
                .thenReturn(List.of(training2));
        when(mockTraineeService.selectByUsername(trainee4_Username, trainee4_Password)).thenReturn(trainee4);
        assertEquals(List.of(training2),
                trainingService
                        .getTrainingsByUsernameAndTrainingTypes(
                                trainee4_Username,
                                trainee4_Password,
                                List.of(trainingType2)));
    }


    @Test
    public void testCreate() {
        Training training = new Training();
        Trainer trainer = new Trainer();
        trainer.setId(1);
        trainer.setUser(new User(1, trainer1_FirstName, trainer1_LastName, trainer1_Username, trainer1_Password, trainer1_Active));
        trainer.setSpecialization(trainingType1);
        Trainee trainee = new Trainee();
        trainee.setId(1);
        trainee.setUser(new User(3, trainee3_FirstName, trainee3_LastName, trainee3_Username, trainee3_Password, trainee3_Active));
        trainee.setAddress(trainee3_Address);
        trainee.setDateOfBirth(trainee3_Birthday);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingName("name");
        training.setDuration(traning1_Duration);
        training.setTrainingDate(traning1_Date);
        training.setTrainingType(trainingType1);
        Training training2 = new Training(1,  training.getTrainee(), training.getTrainer(),
                training.getTrainingName(), training.getTrainingType(), training.getTrainingDate(),
                training.getDuration());
        when(mockTrainingDaoImpl.create(training)).thenReturn(training2);
        when(mockTrainerService.selectByUsername(trainer1_Username, trainer1_Password)).thenReturn(trainer);
        Training newTraining = trainingService.create(trainer.getUser().getUsername(), trainer.getUser().getPassword(),
                trainee, training.getTrainingName(), trainingType1, traning1_Date,
                traning1_Duration);
        training.setId(1);
        assertEquals(training, newTraining);
    }

}