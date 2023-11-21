package org.epam.service;

import org.epam.config.security.PasswordChecker;
import org.epam.dao.TrainingDaoImpl;
import org.epam.dao.UserDaoImpl;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.UserSetter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.epam.TestDatabaseInitializer.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainingServiceTest {

    @Mock
    private static TrainingDaoImpl mockTrainingDao = mock(TrainingDaoImpl.class);

    @Mock
    private static TrainerService mockTrainerService = mock(TrainerService.class);

    @Mock
    private static TraineeService mockTraineeService = mock(TraineeService.class);

    @Mock
    private static PasswordChecker mockPasswordChecker = mock(PasswordChecker.class);

    @Mock
    private static UserSetter userSetter = mock(Trainer.class);

    @Mock
    private static UserDaoImpl mockUserDao = mock(UserDaoImpl.class);

    private static final TrainingService trainingService = new TrainingService();
    @BeforeAll
    static void setUp() {
        trainingService.setTrainingDao(mockTrainingDao);
        trainingService.setPasswordChecker(mockPasswordChecker);
        trainingService.setTraineeService(mockTraineeService);
        trainingService.setTrainerService(mockTrainerService);
        trainingService.setUserDao(mockUserDao);
    }

    @Test
    public void testGetAllTrainersAvalibleForTrainee() {
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(trainer1);
        trainers.add(trainer2);
        when(mockUserDao.getByUsername(trainee3_Username)).thenReturn(user3);
        when(mockTrainerService.selectAll()).thenReturn(trainers);
        when(mockTrainingDao.getAllTrainersAvalibleForTrainee(trainee3, trainers)).thenReturn(List.of(trainer2));
        assertEquals(List.of(trainer2), trainingService.getAllTrainersAvalibleForTrainee(trainee3, trainee3_Username, trainee3_Password));
    }

    @Test
    public void testGetTrainingsByTRAINERusernameAndTrainingTypes() {
        when(mockUserDao.getByUsername(trainer2_Username)).thenReturn(user2);
        when(mockTrainingDao.getAllByUsernameAndTrainingTypes(trainer2_Username, List.of(trainingType2))).thenReturn(List.of(training2));
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
        when(mockTrainingDao.getAllByUsernameAndTrainingTypes(trainee4_Username, List.of(trainingType2))).thenReturn(List.of(training2));
        assertEquals(List.of(training2),
                trainingService
                        .getTrainingsByUsernameAndTrainingTypes(
                                trainee4_Username,
                                trainee4_Password,
                                List.of(trainingType2)));
    }


    @Test
    public void testCreate() {
        Training training = training1;
        when(mockTrainingDao.get(1)).thenReturn(training);
        when(mockTrainingDao.getModelByUsername(trainer1_Username)).thenReturn(trainer1);
        trainingService.create(trainer1_Username, trainer1_Password,
                trainee6, trainingName1.name(), trainingType1, traning1_Date,
                traning1_Duration);
        assertEquals(training, trainingService.select(1));
    }

    @Test
    public void testSelect() {
        Training training = new Training();
        training.setId(1);
        training.setTrainer(training1.getTrainer());
        training.setTrainee(training1.getTrainee());
        when(mockTrainingDao.get(1)).thenReturn(training);
        assertEquals(training, trainingService.select(1));
    }
}