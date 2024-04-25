package org.epam.gymservice.service.asyncMessaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SQSServiceTest {

    @Mock
    private SqsClient sqsClient;
    @Mock
    private TrainerService trainerService;
    @Mock
    private TraineeService traineeService;
    @Spy
    TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);
    @Spy
    @InjectMocks
    TrainingMapper trainingMapper = Mappers.getMapper(TrainingMapper.class);
    @Mock
    private ObjectMapper mapper;
    @InjectMocks
    private SQSService sqsService;
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
    void testAddWorkload() throws JsonProcessingException {
        when(mapper.writeValueAsString(any(TrainerWorkloadRequest.class))).thenReturn("message");
        when(trainerService.selectByUsername(anyString())).thenReturn(trainer);
        sqsService.addWorkload(request);
        verify(sqsClient, times(1)).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    void testDeleteAllWorkload() throws JsonProcessingException {
        List<TrainingDto> trainingList = new ArrayList<>();
        String deletingTraineeUsername = "trainee1";
        TrainingDto training = new TrainingDto();
        training.setOpponentName("trainer1");
        trainingList.add(training);
        GetTrainingsResponse getTrainingsResponse = new GetTrainingsResponse();
        getTrainingsResponse.setTrainings(trainingList);
        when(traineeService.getTraineeTrainingsList(anyString(), any(GetTraineeTrainingsListRequest.class))).thenReturn(getTrainingsResponse);
        when(trainerService.selectByUsername(anyString())).thenReturn(trainer);
        when(mapper.writeValueAsString(any(TrainerWorkloadRequest.class))).thenReturn("message");
        sqsService.deleteAllWorkload(deletingTraineeUsername);
        verify(sqsClient, times(1)).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    void testDeleteWorkload() throws JsonProcessingException {
        TrainerWorkloadRequest request = new TrainerWorkloadRequest();
        when(mapper.writeValueAsString(any(TrainerWorkloadRequest.class))).thenReturn("message");
        sqsService.deleteWorkload(request);
        verify(sqsClient, times(1)).sendMessage(any(SendMessageRequest.class));
    }

}