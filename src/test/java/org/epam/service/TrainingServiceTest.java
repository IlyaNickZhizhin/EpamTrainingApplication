package org.epam.service;

import org.epam.Supplier;
import org.epam.dao.TrainingDaoImpl;
import org.epam.model.gymModel.Training;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.nio.file.AccessDeniedException;

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
    public void testCreate() throws AccessDeniedException {
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
        assertEquals(training, trainingService.select(1,
                Supplier.training1.getTrainer().getUser().getUsername(),
                Supplier.training1.getTrainer().getUser().getPassword()));
    }

    @Test
    public void testSelect() throws AccessDeniedException {
        Training training = new Training();
        training.setId(1);
        training.setTrainer(Supplier.training1.getTrainer());
        training.setTrainee(Supplier.training1.getTrainee());
        when(mockTrainingDao.get(1)).thenReturn(training);
        assertEquals(training, trainingService.select(1,
                Supplier.training1.getTrainer().getUser().getUsername(),
                Supplier.training1.getTrainer().getUser().getPassword()));
    }
}