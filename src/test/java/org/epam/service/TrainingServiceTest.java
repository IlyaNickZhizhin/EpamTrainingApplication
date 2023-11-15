package org.epam.service;

import org.epam.dao.gymDao.TrainingDao;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainingServiceTest {

    @Mock
    private static TrainingDao mockTrainingDao = mock(TrainingDao.class);

    @Mock
    private static TraineeService mockTraineeService = mock(TraineeService.class);

    @Mock
    private static TrainerService mockTrainerService = mock(TrainerService.class);

    private static final TrainingService trainingService = new TrainingService();
    @BeforeAll
    static void setUp() {
        trainingService.setTrainingDao(mockTrainingDao);
        trainingService.setTraineeService(mockTraineeService);
        trainingService.setTrainerService(mockTrainerService);
    }

    @Test
    public void testCreate() {
        Training training = new Training();
        training.setId(1);
        String name = "Test";
        Date trainingDate = new Date();
        Number duration = 60;
        Trainer trainer = new Trainer(TrainingType.CARDIO.getName(), 1);
        Trainee trainee = new Trainee();
        trainee.setId(1);
        TrainingType trainingType = TrainingType.CARDIO;
        when(mockTrainingDao.get(1)).thenReturn(training);
        trainingService.create(name, trainingDate, duration, trainer, trainee, trainingType);
        assertEquals(training, trainingService.select(1));
    }

    @Test
    public void testSelect() {
        Training training = new Training();
        training.setId(1);
        when(mockTrainingDao.get(1)).thenReturn(training);
        assertEquals(training, trainingService.select(1));
    }
}
