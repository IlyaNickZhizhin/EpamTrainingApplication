package org.epam.service;

import org.epam.Supplier;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.TrainingDaoImpl;
import org.epam.dao.UserDaoImpl;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.epam.model.gymModel.UserSetter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.epam.Supplier.trainee3;
import static org.epam.Supplier.trainee3_Password;
import static org.epam.Supplier.trainee3_Username;
import static org.epam.Supplier.trainer1;
import static org.epam.Supplier.trainer1_Password;
import static org.epam.Supplier.trainer1_Username;
import static org.epam.Supplier.trainer2;
import static org.epam.Supplier.trainingName1;
import static org.epam.Supplier.trainingType1;
import static org.epam.Supplier.traning1_Date;
import static org.epam.Supplier.traning1_Duration;
import static org.epam.Supplier.user1;
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
        when(mockTrainingDao.getAllTrainersAvalibleForTrainee(trainee3, trainers));
        assertEquals(List.of(trainer2), trainingService.getAllTrainersAvalibleForTrainee(trainee3, trainee3_Username, trainee3_Password));
    }

    @Test
    public void testGetTrainingsByTrainerAndTrainingTypesForTrainer() {

        assertEquals(trainer1,trainingService.getTrainingsByTrainerAndTrainingTypesForTrainer(trainer1_Username, trainer1_Password, List.of(trainingType1)));
    }

    @Test
    public void testGetTrainingsByTraineeAndTrainingTypesForTrainee() {
        List<TrainingType> trainingTypes = new ArrayList<>();
        trainingTypes.add(new TrainingType());
        trainingTypes.add(new TrainingType());
        String username = "username";
        String password = "password";
        List<Training> trainings = new ArrayList<>();
        trainings.add(new Training());
        trainings.add(new Training());
        when(trainerDao.getTrainingsByTraineeAndTrainingTypesForTrainee(trainingTypes, username, password)).thenReturn(trainings);
        assertEquals(trainings, trainerService.getTrainingsByTraineeAndTrainingTypesForTrainee(trainingTypes, username, password));
    }


    @Test
    public void testCreate() {
        Training training = Supplier.training1;
        when(mockTrainingDao.get(1)).thenReturn(training);
        when(mockUserDao.whoIsUser(trainer1_Username)).thenReturn(trainer1);
        trainingService.create(trainer1_Username, trainer1_Password,
                trainee3, trainingName1.name(), trainingType1, traning1_Date,
                traning1_Duration);
        assertEquals(training, trainingService.select(1));
    }

    @Test
    public void testSelect() {
        Training training = new Training();
        training.setId(1);
        training.setTrainer(Supplier.training1.getTrainer());
        training.setTrainee(Supplier.training1.getTrainee());
        when(mockTrainingDao.get(1)).thenReturn(training);
        assertEquals(training, trainingService.select(1));
    }
}