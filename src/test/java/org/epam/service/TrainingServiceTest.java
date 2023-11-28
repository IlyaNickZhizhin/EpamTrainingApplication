package org.epam.service;

import org.epam.Reader;
import org.epam.TestMapper;
import org.epam.dao.TrainingDaoImpl;
import org.epam.dao.UserDaoImpl;
import org.epam.dto.LoginRequest;
import org.epam.dto.trainingDto.GetTrainingTypesResponse;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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
    private GymGeneralService<Trainer> mockTrainerService = mock(GymGeneralService.class);

    @Mock
    private GymGeneralService<Trainee> mockTraineeService = mock(GymGeneralService.class);

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
    void testSelectAllTrainingTypes() {
        List<TrainingType> types = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            types.add(reader.readEntity("trainingTypes/trainingType" + i, TrainingType.class));
        }
        GetTrainingTypesResponse response = new GetTrainingTypesResponse();
        response.setTrainingTypes(types);
        when(mockTrainingDaoImpl.getAllTrainingTypes()).thenReturn(types);
        assertEquals(response, trainingService.selectAllTrainingTypes());
    }

    @Test
    void create() {
    }

    @Test
    void updateTrainersList() {
    }

    @Test
    void getTrainersList() {
    }

    @Test
    void getNotAssignedOnTraineeActiveTrainers() {
    }

    @Test
    void getTraineeTrainingsList() {
    }

    @Test
    void getTrainerTrainingsList() {
    }
}