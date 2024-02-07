package org.epam.gymservice.service.asyncMessaging;

import org.epam.common.dto.TrainerWorkloadRequest;
import org.epam.gymservice.Reader;
import org.epam.gymservice.dto.trainerDto.TrainerProfileResponse;
import org.epam.gymservice.dto.trainingDto.AddTrainingRequest;
import org.epam.gymservice.dto.trainingDto.GetTraineeTrainingsListRequest;
import org.epam.gymservice.dto.trainingDto.GetTrainingsResponse;
import org.epam.gymservice.dto.trainingDto.TrainingDto;
import org.epam.gymservice.mapper.TrainerMapper;
import org.epam.gymservice.mapper.TrainingMapper;
import org.epam.gymservice.model.gymModel.Trainer;
import org.epam.gymservice.model.gymModel.Training;
import org.epam.gymservice.service.TraineeService;
import org.epam.gymservice.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActiveMqServiceTest {

    @Mock
    private JmsTemplate jmsTemplate;
    @Mock
    private TrainerService trainerService;
    @Mock
    private TraineeService traineeService;
    @InjectMocks
    private ActiveMqService activeMqService;
    @Spy
    TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);
    @Spy
    @InjectMocks
    TrainingMapper trainingMapper = Mappers.getMapper(TrainingMapper.class);
    Reader reader = new Reader();
    AddTrainingRequest request;
    TrainerProfileResponse trainer;


    @BeforeEach
    void setUp() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        request = reader.readDto("trainings/training1", Training.class, trainingMapper::trainingToAddTrainingRequest);
        trainer = reader.readDto("trainers/trainer1", Trainer.class, trainerMapper::trainerToProfileResponse);
    }

    @Test
    void testAddWorkload() {
        AddTrainingRequest request = new AddTrainingRequest();
        request.setTrainerUsername("trainer1");
        TrainerProfileResponse trainer = new TrainerProfileResponse();
        when(trainerService.selectByUsername(request.getTrainerUsername())).thenReturn(trainer);
        activeMqService.addWorkload(request);
        verify(jmsTemplate, times(1)).convertAndSend(anyString(), any(TrainerWorkloadRequest.class), any(MessagePostProcessor.class));
    }

    @Test
    void testDeleteAllWorkload() {
        List<TrainingDto> trainingList = new ArrayList<>();
        String deletingTraineeUsername = "trainee1";
        TrainingDto training = new TrainingDto();
        training.setOpponentName("trainer1");
        trainingList.add(training);
        GetTrainingsResponse getTrainingsResponse = new GetTrainingsResponse();
        getTrainingsResponse.setTrainings(trainingList);
        TrainerProfileResponse trainer = new TrainerProfileResponse();
        trainer.setFirstName("trainer1");
        when(traineeService.getTraineeTrainingsList("trainee1", GetTraineeTrainingsListRequest.of(LocalDate.now()))).thenReturn(getTrainingsResponse);
        when(trainerService.selectByUsername("trainer1")).thenReturn(trainer);
        activeMqService.deleteAllWorkload(deletingTraineeUsername);
        verify(jmsTemplate, times(1)).convertAndSend(anyString(), any(TrainerWorkloadRequest.class), any(MessagePostProcessor.class));
    }

    @Test
    void testDeleteWorkload() {
        TrainerWorkloadRequest request = new TrainerWorkloadRequest();
        activeMqService.deleteWorkload(request);
        verify(jmsTemplate, times(1)).convertAndSend(anyString(), any(TrainerWorkloadRequest.class), any(MessagePostProcessor.class));
    }
}