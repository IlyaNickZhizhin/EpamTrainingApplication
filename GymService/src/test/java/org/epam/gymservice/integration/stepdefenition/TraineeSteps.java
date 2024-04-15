package org.epam.gymservice.integration.stepdefenition;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.epam.gymservice.controller.TraineeController;
import org.epam.gymservice.dto.ActivateDeactivateRequest;
import org.epam.gymservice.dto.ChangeLoginRequest;
import org.epam.gymservice.dto.RegistrationResponse;
import org.epam.gymservice.dto.traineeDto.TraineeProfileResponse;
import org.epam.gymservice.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.gymservice.dto.traineeDto.UpdateTraineeProfileRequest;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@CucumberContextConfiguration
@SpringBootTest
public class TraineeSteps {
    @Autowired
    private TraineeController traineeController;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private static JmsTemplate jmsTemplate;
    private MockMvc mvc;
    private ResponseEntity<RegistrationResponse> registrationResponse;
    private TraineeRegistrationRequest registrationRequest;
    private ResponseEntity<Boolean> activationResponse;
    private ResponseEntity<TraineeProfileResponse> profileResponse;
    private ResponseEntity<Boolean> deletionResponse;
    private ResponseEntity<Boolean> passwordChangeResponse;
    private ResponseEntity<GetTrainingsResponse> trainingsResponse;

    private static String username1, username2, password1, password2;


    @BeforeAll
    public static void init(){
        doNothing().when(jmsTemplate).convertAndSend(anyString(), any(), any(MessagePostProcessor.class));
    }

    @Given("a user with name {string}, surname {string}")
    public void registerTrainee(String name, String surname) {
        registrationRequest = new TraineeRegistrationRequest();
        registrationRequest.setFirstname(name);
        registrationRequest.setLastname(surname);
    }

    @When("the user tries to register")
    public void whenUserTriesToRegister() {
        registrationResponse = traineeController.register(registrationRequest);
    }

    @Then("the registration should be successful, trainee with username {string} with generated password of length {int} was added to database")
    public void thenRegistrationSuccessful(String username, int passwordLength) {
        assertEquals(HttpStatus.CREATED, registrationResponse.getStatusCode());
        assertEquals(username, Objects.requireNonNull(registrationResponse.getBody()).getUsername());
        assertEquals(passwordLength, registrationResponse.getBody().getPassword().length());
        if (username1 == null) {
            username1=registrationResponse.getBody().getUsername();
            password1 =registrationResponse.getBody().getPassword();
        } else {
            username2 = registrationResponse.getBody().getUsername();
            password2 = registrationResponse.getBody().getPassword();
        }
    }

    @Given("a registered trainee with username from previous test")
    public void givenRegisteredTrainee() {
    }


    @When("the trainee tries to change their password to {string}")
    public void whenTraineeTriesToChangePassword(String newPassword) {
        ChangeLoginRequest passwordChangeRequest = new ChangeLoginRequest();
        passwordChangeRequest.setUsername(username1);
        passwordChangeRequest.setNewPassword(newPassword);
        passwordChangeRequest.setOldPassword(password1);
        passwordChangeRequest.setNewPassword(newPassword);
        authSimulationTrainee(username1, password1);
        passwordChangeResponse = traineeController.changePassword(passwordChangeRequest);
        if(passwordChangeResponse.getBody().booleanValue()) password1 = newPassword;
    }

    @Then("trainees password change should be successful")
    public void thenPasswordChangeSuccessful() {
        assertEquals(HttpStatus.OK, passwordChangeResponse.getStatusCode());
        assertEquals(Boolean.TRUE, passwordChangeResponse.getBody());
    }

    @When("the trainee tries to change their password to {string} using wrong old password")
    public void whenTraineeTriesToChangePasswordWithWrongOldPassword(String newPassword) {
        ChangeLoginRequest passwordChangeRequest = new ChangeLoginRequest();
        passwordChangeRequest.setUsername(username1);
        passwordChangeRequest.setNewPassword(newPassword);
        passwordChangeRequest.setOldPassword("wrongpassword");
        authSimulationTrainee(username1, password1);
        passwordChangeResponse = traineeController.changePassword(passwordChangeRequest);
    }

    @Then("trainees password change should be unsuccessful")
    public void thenPasswordChangeUnsuccessful() {
        assertEquals(HttpStatus.BAD_REQUEST, passwordChangeResponse.getStatusCode());
        assertEquals(Boolean.FALSE, passwordChangeResponse.getBody());
    }

    @Given("an active trainee with username from previous test")
    public void givenActiveTrainee() {
        // the trainee is already active after previous scenario
    }

    @When("the admin tries to deactivate the trainee")
    public void whenAdminTriesToDeactivateTrainee() {
        ActivateDeactivateRequest request = new ActivateDeactivateRequest();
        request.setUsername(username1);
        request.setActive(false);
        authSimulationAdmin(username2, password2);
        activationResponse = traineeController.setActive(request);
    }

    @Given("a trainee tryes to change active to another trainee")
    public void givenAnotherActiveTrainee() {
        // the trainee is already exist after previous scenario
    }

    @When("the trainee tries to deactivate another trainee")
    public void whenNotAdminTriesToDeactivateTrainee() {
        ActivateDeactivateRequest request = new ActivateDeactivateRequest();
        request.setUsername(username1);
        request.setActive(false);
        authSimulationTrainee(username2, password2);
        try {activationResponse = traineeController.setActive(request);
        } catch (org.springframework.security.access.AccessDeniedException ignored) {
            activationResponse = new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
        }
    }

    @Then("the deactivation should be successful")
    public void thenDeactivationSuccessful() {
        assertEquals(HttpStatus.OK, activationResponse.getStatusCode());
        assertEquals(Boolean.TRUE, activationResponse.getBody());
    }

    @Then("the deactivation should be unsuccessful")
    public void thenDeactivationUnsuccessful() {
        assertEquals(HttpStatus.BAD_REQUEST, activationResponse.getStatusCode());
    }

    @Given("an inactive trainee with username from previous test")
    public void givenInactiveTrainee() {
        // the trainee is already inactive after previous scenario
    }

    @When("the admin tries to activate the trainee")
    public void whenAdminTriesToActivateTrainee() {
        ActivateDeactivateRequest request = new ActivateDeactivateRequest();
        request.setUsername(username1);
        request.setActive(true);
        authSimulationAdmin(username2, password2);
        activationResponse = traineeController.setActive(request);
    }

    @Then("the activation should be successful")
    public void thenActivationSuccessful() {
        assertEquals(HttpStatus.OK, activationResponse.getStatusCode());
        assertEquals(Boolean.TRUE, activationResponse.getBody());
    }

    @When("the trainee tries to update their profile")
    public void whenTraineeTriesToUpdateProfile() {
        UpdateTraineeProfileRequest updateRequest = new UpdateTraineeProfileRequest();
        updateRequest.setUsername(username1);
        updateRequest.setFirstname(username1.split("\\.")[0]);
        updateRequest.setLastname(username1.split("\\.")[1]+"Ee");
        updateRequest.setActive(true);
        authSimulationTrainee(username1, password1);
        profileResponse = traineeController.update(updateRequest);
    }

    @Then("the trainee profile update should be successful")
    public void thenProfileUpdateSuccessful() {
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode());
        assertEquals(username1.split("\\.")[1]+"Ee", profileResponse.getBody().getLastname());
    }

    @When("the trainee tries to select their profile by username")
    public void whenTraineeTriesToSelectProfileByUsername() {
        authSimulationTrainee(username1, password1);
        profileResponse = traineeController.selectByUsername(username1);
    }

    @Then("the trainee profile selection should be successful")
    public void thenProfileSelectionSuccessful() {
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode());
        assertEquals(username1.split("\\.")[0], Objects.requireNonNull(profileResponse.getBody().getFirstname()));
    }

    @When("the trainee tries to get their trainings list")
    public void whenTraineeTriesToGetTrainingsList() {
        authSimulationTrainee(username1, password1);
        trainingsResponse = traineeController.getTraineeTrainingsList(username1, null, null, null, null);
    }

    @Then("trainees trainings list retrieval should be successful")
    public void thenTrainingsListRetrievalSuccessful() {
        assertEquals(HttpStatus.OK, trainingsResponse.getStatusCode());
        assertNotNull(trainingsResponse.getBody().getTrainings());
    }

    @When("the trainee tries to update their profile with invalid username")
    public void whenTraineeTriesToUpdateProfileWithInvalidUsername() {
        UpdateTraineeProfileRequest updateRequest = new UpdateTraineeProfileRequest();
        updateRequest.setUsername("invalid_username");
        authSimulationAdmin(username1, password1);
        try {
            profileResponse = traineeController.update(updateRequest);
        } catch (Exception e) {
            profileResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Then("the profile update should be unsuccessful")
    public void thenProfileUpdateUnsuccessful() {
        assertEquals(HttpStatus.BAD_REQUEST, profileResponse.getStatusCode());
    }

    @When("the trainee tries to select their profile by invalid username")
    public void whenTraineeTriesToSelectProfileByInvalidUsername() {
        authSimulationAdmin(username1, password1);
        profileResponse = traineeController.selectByUsername("invalid_username");
    }

    @Then("the profile selection should be unsuccessful")
    public void thenProfileSelectionUnsuccessful() {
        assertEquals(HttpStatus.BAD_REQUEST, profileResponse.getStatusCode());
    }

    @When("the trainee tries to get their trainings list with invalid username")
    public void whenTraineeTriesToGetTrainingsListWithInvalidUsername() {
        authSimulationAdmin(username1, password1);
        trainingsResponse = traineeController.getTraineeTrainingsList("invalid_username", null, null, null, null);
    }

    @Then("trainees trainings list retrieval should be unsuccessful")
    public void thenTrainingsListRetrievalUnsuccessful() {
        assertEquals(HttpStatus.BAD_REQUEST, trainingsResponse.getStatusCode());
    }

    @When("the trainee tries to delete their profile")
    public void whenTraineeTriesToDeleteProfile() {
        authSimulationTrainee(username1, password1);
        deletionResponse = traineeController.delete(username1);
    }

    @Then("the profile deletion should be successful")
    public void thenProfileDeletionSuccessful() {
        assertEquals(HttpStatus.OK, deletionResponse.getStatusCode());
        assertEquals(Boolean.TRUE, deletionResponse.getBody());
    }

    @When("the trainee tries to delete their profile with invalid username")
    public void whenTraineeTriesToDeleteProfileWithInvalidUsername() {
        authSimulationTrainee("invalid_username", password2);
        deletionResponse = traineeController.delete("invalid_username");
    }

    @Then("the profile deletion should be unsuccessful")
    public void thenProfileDeletionUnsuccessful() {
        assertEquals(HttpStatus.BAD_REQUEST, deletionResponse.getStatusCode());
    }

    private void authSimulationTrainee(String username, String password){
        UserDetails userDetails = User
                .withUsername(username)
                .password(password)
                .roles(Role.of(Role.Authority.ROLE_TRAINEE).toString())
                .build();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void authSimulationAdmin(String username, String password){
        UserDetails userDetails = User
                .withUsername(username)
                .password(password)
                .roles(Role.of(Role.Authority.ROLE_ADMIN).toString())
                .build();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
