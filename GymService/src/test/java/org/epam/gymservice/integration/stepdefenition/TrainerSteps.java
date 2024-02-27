package org.epam.gymservice.integration.stepdefenition;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.epam.gymservice.controller.TrainerController;
import org.epam.gymservice.dto.ChangeLoginRequest;
import org.epam.gymservice.dto.RegistrationResponse;
import org.epam.gymservice.dto.trainerDto.TrainerProfileResponse;
import org.epam.gymservice.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.gymservice.dto.trainerDto.UpdateTrainerProfileRequest;
import org.epam.gymservice.dto.trainingDto.GetTrainingsResponse;
import org.epam.gymservice.model.Role;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TrainerSteps {
    
    @Autowired
    private TrainerController trainerController;

    @MockBean
    private static JmsTemplate jmsTemplate;

    private ResponseEntity<RegistrationResponse> registrationResponse;
    private ResponseEntity<Boolean> passwordChangeResponse;
    private ResponseEntity<TrainerProfileResponse> profileResponse;
    private ResponseEntity<GetTrainingsResponse> trainingsResponse;
    private TrainerRegistrationRequest registrationRequest;
    private static String username1, password1;

    @BeforeAll
    public static void init() {
        doNothing().when(jmsTemplate).convertAndSend(anyString(), any(), any(MessagePostProcessor.class));
    }

    @Given("a user with name {string}, surname {string}, going to register as trainer")
    public void registerTrainer(String name, String surname) {
        registrationRequest = new TrainerRegistrationRequest();
        registrationRequest.setFirstName(name);
        registrationRequest.setLastName(surname);
    }

    @When("the user tries to register as a trainer")
    public void whenUserTriesToRegisterAsTrainer() {
        registrationResponse = trainerController.register(registrationRequest);
    }

    @Then("the registration should be successful, trainer with username {string} with generated password of length {int} was added to database")
    public void thenRegistrationSuccessful(String username, int passwordLength) {
        assertEquals(HttpStatus.CREATED, registrationResponse.getStatusCode());
        assertEquals(username, Objects.requireNonNull(registrationResponse.getBody()).getUsername());
        assertEquals(passwordLength, registrationResponse.getBody().getPassword().length());
        username1 = registrationResponse.getBody().getUsername();
        password1 = registrationResponse.getBody().getPassword();
    }

    @Given("a registered trainer with username from previous test")
    public void givenRegisteredTrainer() {}

    @When("the trainer tries to change their password to {string}")
    public void whenTrainerTriesToChangePassword(String newPassword) {
        ChangeLoginRequest passwordChangeRequest = new ChangeLoginRequest();
        passwordChangeRequest.setUsername(username1);
        passwordChangeRequest.setNewPassword(newPassword);
        passwordChangeRequest.setOldPassword(password1);
        authSimulation(username1, password1);
        passwordChangeRequest.setNewPassword(newPassword);
        if(trainerController.changePassword(passwordChangeRequest).getBody().booleanValue()) password1 = newPassword;
    }

    @Then("the password change should be successful")
    public void thenPasswordChangeSuccessful() {
        assertEquals(HttpStatus.OK, passwordChangeResponse.getStatusCode());
        assertEquals(Boolean.TRUE, passwordChangeResponse.getBody());
    }

    @When("the trainer tries to update their profile")
    public void whenTrainerTriesToUpdateProfile() {
        UpdateTrainerProfileRequest updateRequest = new UpdateTrainerProfileRequest();
        updateRequest.setUsername(username1);
        authSimulation(username1, password1);
        profileResponse = trainerController.update(updateRequest);
    }

    @Then("the profile update should be successful")
    public void thenProfileUpdateSuccessful() {
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode());
        assertEquals(username1.split("\\.")[0], Objects.requireNonNull(profileResponse.getBody().getFirstName()));
    }

    @When("the trainer tries to select their profile by username")
    public void whenTrainerTriesToSelectProfileByUsername() {
        authSimulation(username1, password1);
        profileResponse = trainerController.selectByUsername(username1);
    }

    @Then("the profile selection should be successful")
    public void thenProfileSelectionSuccessful() {
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode());
        assertEquals(username1.split("\\.")[0], Objects.requireNonNull(profileResponse.getBody().getFirstName()));
    }

    @When("the trainer tries to get their trainings list")
    public void whenTrainerTriesToGetTrainingsList() {
        authSimulation(username1, password1);
        trainingsResponse = trainerController.getTrainerTrainingsList(username1, null, null, null);
    }

    @Then("the trainings list retrieval should be successful")
    public void thenTrainingsListRetrievalSuccessful() {
        assertEquals(HttpStatus.OK, trainingsResponse.getStatusCode());
        assertNotNull(trainingsResponse.getBody().getTrainings());
    }

    private void authSimulation(String username, String password) {
        UserDetails userDetails = User.withUsername(username).password(password).roles(Role.of(Role.Authority.TRAINER).toString()).build();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}

