package org.epam.service.storageService;

import org.epam.TestDatabaseInitializer;
import org.epam.testBeans.dao.gymDaoFileStorage.TrainingDaoStorageImpl;
import org.epam.model.gymModel.Training;
import org.epam.testBeans.service.TraineeStorageService;
import org.epam.testBeans.service.TrainerStorageService;
import org.epam.testBeans.service.TrainingStorageService;
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
        Training training = TestDatabaseInitializer.training1;
        when(mockTrainingDaoStorageImpl.get(1)).thenReturn(training);
        trainingService.create(
            TestDatabaseInitializer.training1.getTrainingName(),
            TestDatabaseInitializer.training1.getTrainingDate(),
            TestDatabaseInitializer.training1.getDuration(),
            TestDatabaseInitializer.training1.getTrainer(),
            TestDatabaseInitializer.training1.getTrainee(),
            TestDatabaseInitializer.training1.getTrainingType()
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
