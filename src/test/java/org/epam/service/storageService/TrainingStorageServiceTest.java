package org.epam.service.storageService;

import org.epam.Supplier;
import org.epam.dao.storage.gymDaoFileStorage.TrainingDaoStorageImpl;
import org.epam.model.gymModel.Training;
import org.epam.service.storage.TraineeStorageService;
import org.epam.service.storage.TrainerStorageService;
import org.epam.service.storage.TrainingStorageService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainingStorageServiceTest {

    @Mock
    private static TrainingDaoStorageImpl mockTrainingDaoStorageImpl = mock(TrainingDaoStorageImpl.class);
    @Mock
    private static TraineeStorageService mockTraineeService = mock(TraineeStorageService.class);

    @Mock
    private static TrainerStorageService mockTrainerService = mock(TrainerStorageService.class);

    private static final TrainingStorageService trainingService = new TrainingStorageService();
    @BeforeAll
    static void setUp() {
        trainingService.setTrainingDao(mockTrainingDaoStorageImpl);
        trainingService.setTraineeService(mockTraineeService);
        trainingService.setTrainerService(mockTrainerService);
    }

    @Test
    public void testCreate() {
        Training training = Supplier.training1;
        when(mockTrainingDaoStorageImpl.get(1)).thenReturn(training);
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
        when(mockTrainingDaoStorageImpl.get(1)).thenReturn(training);
        assertEquals(training, trainingService.select(1));
    }
}
