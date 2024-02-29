package org.epam.gymservice.integration.stepdefenition;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.epam.gymservice.Reader;
import org.epam.gymservice.controller.TrainingController;
import org.epam.gymservice.dto.trainerDto.TrainerDto;
import org.epam.gymservice.dto.trainingDto.AddTrainingRequest;
import org.epam.gymservice.dto.trainingDto.GetTrainersResponse;
import org.epam.gymservice.dto.trainingDto.GetTrainingTypesResponse;
import org.epam.gymservice.dto.trainingDto.UpdateTraineeTrainerListRequest;
import org.epam.gymservice.mapper.TrainerMapper;
import org.epam.gymservice.mapper.TrainingMapper;
import org.epam.gymservice.model.Role;
import org.epam.gymservice.model.gymModel.Trainer;
import org.epam.gymservice.model.gymModel.Training;
import org.epam.gymservice.model.gymModel.TrainingType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
public class TrainingSteps {

    @Autowired
    TrainingController trainingController;
    @MockBean
    private static JmsTemplate jmsTemplate;
    private static Reader reader = new Reader("src/test/resources/models/", ".json");
    TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);
    TrainingMapper trainingMapper = Mappers.getMapper(TrainingMapper.class);

    UpdateTraineeTrainerListRequest updateTraineeTrainerListRequest;

    private ResponseEntity<AddTrainingRequest> addTrainingResponse;
    private ResponseEntity<GetTrainingTypesResponse> getTrainingTypesResponse;
    private ResponseEntity<GetTrainersResponse> getTrainersResponse;

    @BeforeAll
    public static void setUp() {
        doNothing().when(jmsTemplate).convertAndSend(anyString(), any(), any(MessagePostProcessor.class));
    }

    @BeforeEach
    public void init(){

    }

    @Given("a active trainer with username {string} and password {string}")
    public void givenTrainer(String username, String password) {
        authSimulationAsTrainer(username, password);
    }

    @Given("a active trainee with username {string} and password {string}")
    public void givenTrainee(String username, String password) {
        updateTraineeTrainerListRequest = new UpdateTraineeTrainerListRequest();
        updateTraineeTrainerListRequest.setTraineeUsername(username);
        authSimulationAsTrainee(username, password);
    }

    @Given("a active admin with username {string} and password {string}")
    public void givenAdmin(String username, String password) {
        updateTraineeTrainerListRequest = new UpdateTraineeTrainerListRequest();
        updateTraineeTrainerListRequest.setTraineeUsername(username);
        authSimulationAsAdmin(username, password);
    }

    @When("the user makes request to create {string} \\(for himself)")
    public void whenUserMakesCorrectPostRequest(String url) throws Exception {
        AddTrainingRequest request = reader.readDto(url, Training.class, trainingMapper::trainingToAddTrainingRequest);
        addTrainingResponse = trainingController.create(request);
    }

    @When("the user makes request to create {string} \\(not for himself)")
    public void whenUserMakesIncorrectPostRequest(String url) throws Exception {
        AddTrainingRequest request = reader.readDto(url, Training.class, trainingMapper::trainingToAddTrainingRequest);
        try {
            addTrainingResponse = trainingController.create(request);
        } catch (AccessDeniedException e) {
            addTrainingResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Then("the response status should be {string}")
    public void thenResponseStatusShouldBe(String statusCode) throws Exception {
        assertEquals(statusCode, addTrainingResponse.getStatusCode().toString());
    }

    @Then("the response should contain a training with name {string}")
    public void thenResponseShouldContainTrainingWithName(String trainingName) throws Exception {
        AddTrainingRequest request = reader.readDto("trainings/training1", Training.class, trainingMapper::trainingToAddTrainingRequest);
        assertEquals(addTrainingResponse.getBody().getTrainingName(), request.getTrainingName());
    }

    @When("the user makes request to all training types")
    public void whenUserMakesRequestToAllTrainingTypes() throws Exception {
        authSimulationAsTrainer("any", "pass");
        getTrainingTypesResponse = trainingController.getAllTrainingTypes();
    }

    @Then("the getTrainingTypes response status should be {string}")
    public void thenGetTrainingTypesResponseStatusShouldBe(String statusCode) throws Exception {
        assertEquals(getTrainingTypesResponse.getStatusCode().toString(), statusCode);
    }

    @Then("the response should contain all {int} training types witch contains in {string}")
    public void thenResponseShouldContainAllTrainingTypes(int count, String path) throws Exception {
        for (int i = 1; i <= count; i++) {
            TrainingType trainingType = reader.readEntity(path+i, TrainingType.class);
            assertEquals(getTrainingTypesResponse.getBody().getTrainingTypes().get(i-1), trainingType);
        }
    }

    @When("the user makes a PUT request to update his trainers list with trainer {string}")
    public void whenUserMakesPutRequestToUpdateHisTrainersListForHimself(String trainerUsername) throws Exception {
        List<String> listOfNewTrainers = new ArrayList<>();
        listOfNewTrainers.add(trainerUsername);
        updateTraineeTrainerListRequest.setTrainerUsernames(listOfNewTrainers);
        getTrainersResponse = trainingController.updateTrainersList(updateTraineeTrainerListRequest);
    }

    @When("the user makes a PUT request to update {string}s trainers list with trainer {string}")
    public void whenUserMakesPutRequestToUpdateHisTrainersListNotForHimself(String traineeUsername, String trainerUsername) throws Exception {
        List<String> listOfNewTrainers = new ArrayList<>();
        listOfNewTrainers.add(trainerUsername);
        updateTraineeTrainerListRequest.setTraineeUsername(traineeUsername);
        updateTraineeTrainerListRequest.setTrainerUsernames(listOfNewTrainers);
        try {
            getTrainersResponse = trainingController.updateTrainersList(updateTraineeTrainerListRequest);
        } catch (AccessDeniedException e) {
            getTrainersResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Then("the updateTrainersList response status should be {string}")
    public void thenUpdateTrainersListResponseStatusShouldBe(String statusCode) throws Exception {
        assertEquals(statusCode, getTrainersResponse.getStatusCode().toString());
    }

    @Then("the response should contain the list of trainers {string}")
    public void thenResponseShouldContainListOfTrainers(String trainerPath) throws Exception {
        String[] urlList = trainerPath.split("\\|");
        List<TrainerDto> expected = new ArrayList<>();
        for (String url : urlList) {
            expected.add(reader.readDto(url, Trainer.class, trainerMapper::trainerToTrainerDto));
        }
        for (TrainerDto trainerDto : getTrainersResponse.getBody().getTrainers()){
            assertTrue(expected.contains(trainerDto));
        }
    }

    @When("the user makes a GET request for getting all his trainers")
    public void whenUserMakesGetRequestForGettingAllHisTrainers() throws Exception {
        getTrainersResponse = trainingController.getTrainersList(updateTraineeTrainerListRequest.getTraineeUsername());
    }

    @When("the user makes a GET request for getting all {string}s trainers")
    public void whenUserMakesGetRequestForGettingAllHisTrainersNotForHimself(String username) throws Exception {
        try {
            getTrainersResponse = trainingController.getTrainersList(username);
        } catch (AccessDeniedException e) {
            getTrainersResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Then("the getTrainers response status should be {string}")
    public void thenGetTrainersResponseStatusShouldBe(String statusCode) throws Exception {
        assertEquals(statusCode, getTrainersResponse.getStatusCode().toString());
    }

    @When("the user makes a GET request for getting all available trainers")
    public void whenUserMakesGetRequestForGettingAllAvailableTrainers() throws Exception {
        getTrainersResponse = trainingController.getTrainersList(updateTraineeTrainerListRequest.getTraineeUsername());
    }

    @When("the user makes a GET request for getting all available trainers for {string}")
    public void whenUserMakesGetRequestForGettingAllAvailableTrainersNotForHim(String username) throws Exception {
        try {
            getTrainersResponse = trainingController.getTrainersList(username);
        } catch (AccessDeniedException e) {
            getTrainersResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private void authSimulationAsTrainer(String username, String password) {
        UserDetails userDetails = User.withUsername(username).password(password).roles(Role.of(Role.Authority.TRAINER).toString()).build();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void authSimulationAsTrainee(String username, String password) {
        UserDetails userDetails = User.withUsername(username).password(password).roles(Role.of(Role.Authority.TRAINEE).toString()).build();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void authSimulationAsAdmin(String username, String password) {
        UserDetails userDetails = User.withUsername(username).password(password).roles(Role.of(Role.Authority.ADMIN).toString()).build();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}

