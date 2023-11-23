package org.epam.service;

import org.epam.Reader;
import org.epam.dao.TrainingDaoImpl;
import org.epam.dao.UserDaoImpl;
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

    private final static Reader reader = new Reader();


    @BeforeEach
    void setUp() {
        initMocks(this);
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
    }

    @Test
    public void testGetAllTrainersAvalibleForTrainee() {
        Trainer trainer1 = reader.readTrainer("trainers/trainer1");
        Trainer trainer2 = reader.readTrainer("trainers/trainer2");
        User user3 = reader.readUser("users/user3");
        String trainee3_Username = user3.getUsername();
        String trainee3_Password = user3.getPassword();
        Trainee trainee3 = reader.readTrainee("trainees/trainee3");
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
        Trainer trainer2 = reader.readTrainer("trainers/trainer2");
        User user2 = reader.readUser("users/user2");
        String trainer2_Username = user2.getUsername();
        String trainer2_Password = user2.getPassword();
        Training training2 = reader.readTraining("trainings/training2");
        TrainingType trainingType2 = reader.readType("trainingtypes/trainingType2");
        when(mockUserDao.getByUsername(trainer2_Username)).thenReturn(user2);
        when(mockTrainingDaoImpl
                .getAllByUsernameAndTrainingTypes(List.of(trainingType2), trainer2))
                .thenReturn(List.of(training2));
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
        Trainee trainee4 = reader.readTrainee("trainees/trainee4");
        User user4 = reader.readUser("users/user4");
        String trainee4_Username = user4.getUsername();
        String trainee4_Password = user4.getPassword();
        Training training2 = reader.readTraining("trainings/training2");
        TrainingType trainingType2 = reader.readType("trainingtypes/trainingType2");
        when(mockUserDao.getByUsername(trainee4_Username)).thenReturn(user4);
        when(mockTrainingDaoImpl
                .getAllByUsernameAndTrainingTypes(List.of(trainingType2), trainee4))
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
        Training training = reader.readTraining("trainings/training1");
        TrainingType trainingType1 = reader.readType("trainingtypes/trainingType1");
        Trainer trainer = reader.readTrainer("trainers/trainer1");
        Trainee trainee = reader.readTrainee("trainees/trainee1");
        String trainer1_Username = trainer.getUser().getUsername();
        String trainer1_Password = trainer.getUser().getPassword();
        LocalDate traning1_Date = training.getTrainingDate();
        Double traning1_Duration = training.getDuration();
        training.setId(0);
        when(mockTrainingDaoImpl.create(training)).thenReturn(training);
        when(mockTrainerService.selectByUsername(trainer1_Username, trainer1_Password)).thenReturn(trainer);
        Training newTraining = trainingService.create(trainer.getUser().getUsername(), trainer.getUser().getPassword(),
                trainee, training.getTrainingName(), trainingType1, traning1_Date,
                traning1_Duration);
        training.setId(1);
        assertEquals(training, newTraining);
    }

}