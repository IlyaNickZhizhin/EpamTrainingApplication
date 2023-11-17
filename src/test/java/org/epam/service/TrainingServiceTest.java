package org.epam.service;

import org.epam.Supplier;
import org.epam.dao.TrainingDaoImpl;
import org.epam.model.gymModel.Training;
import org.epam.service.TraineeService;
import org.epam.service.TrainerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

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

    private static final TrainingService trainingService = new TrainingService();
    @BeforeAll
    static void setUp() {
        trainingService.setTrainingDao(mockTrainingDao);
        trainingService.setTraineeService(mockTraineeService);
        trainingService.setTrainerService(mockTrainerService);
    }

    @Test
    public void testCreate() {
        Training training = Supplier.training1;
        when(mockTrainingDao.get(1)).thenReturn(training);
        trainingService.create(
                Supplier.training1.getTrainingName(),
                Supplier.training1.getTrainingDate(),
                Supplier.training1.getDuration(),
                Supplier.training1.getTrainer(),
                Supplier.training1.getTrainee(),
                Supplier.training1.getTrainingType()
        );
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