package org.epam.gymservice.integration.stepdefenition;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.epam.gymservice.controller.TrainerController;
import org.epam.gymservice.dto.ActivateDeactivateRequest;
import org.epam.gymservice.dto.ChangeLoginRequest;
import org.epam.gymservice.dto.RegistrationResponse;
import org.epam.gymservice.dto.trainerDto.TrainerProfileResponse;
import org.epam.gymservice.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.gymservice.dto.trainerDto.UpdateTrainerProfileRequest;
import org.epam.gymservice.dto.trainingDto.GetTrainingsResponse;
import org.epam.gymservice.model.Role;
import org.epam.gymservice.model.gymModel.TrainingType;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TrainerSteps {
    
    @Autowired
    private TrainerController trainerController;
    @MockBean
    private static JmsTemplate jmsTemplate;

    private ResponseEntity<RegistrationResponse> registrationResponse;
    private ResponseEntity<TrainerProfileResponse> profileResponse;
    private ResponseEntity<Boolean> activateDeactivateResponce;
    private TrainerRegistrationRequest registrationRequest;
    private ActivateDeactivateRequest activateDeactivateRequest;
    private ResponseEntity<Boolean> passwordChangeResponse;
    private ResponseEntity<GetTrainingsResponse> trainingsResponse;
    private static String username1, password1;

    @BeforeAll
    public static void init() {
        doNothing().when(jmsTemplate).convertAndSend(anyString(), any(), any(MessagePostProcessor.class));
    }

    @Given("a user with name {string}, surname {string}, specialization {string}")
    public void registerTrainer(String name, String surname, String trainingName) {
        registrationRequest = new TrainerRegistrationRequest();
        try {
            registrationRequest.setFirstName(name);
            registrationRequest.setLastName(surname);
            registrationRequest.setSpecialization(TrainingType.TrainingName.valueOf(trainingName));
        } catch (Exception e) {
            registrationRequest.setSpecialization(null);
        }
    }

    @When("the user tries to register as a trainer")
    public void whenUserTriesToRegisterAsTrainer() {
        try {
            registrationResponse = trainerController.register(registrationRequest);
        } catch (Exception e) {
            registrationResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Then("the registration should be successful, trainer with username {string} with generated password of length {int} was added to database")
    public void thenRegistrationSuccessful(String username, int passwordLength) {
        assertEquals(HttpStatus.CREATED, registrationResponse.getStatusCode());
        assertEquals(username, Objects.requireNonNull(registrationResponse.getBody()).getUsername());
        assertEquals(passwordLength, registrationResponse.getBody().getPassword().length());
        username1 = registrationResponse.getBody().getUsername();
        password1 = registrationResponse.getBody().getPassword();
    }

    @Then("the registration should fail with a {string} error")
    public void thenRegistrationSuccessful(String error) {
        assertEquals(error, registrationResponse.getStatusCode().toString());
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
        passwordChangeResponse = trainerController.changePassword(passwordChangeRequest);
        if(passwordChangeResponse.getBody().booleanValue()) password1 = newPassword;
    }

    @When("the trainer tries to change their password to {string} using wrong old password")
    public void whenTraineeTriesToChangePassword(String newPassword) {
        ChangeLoginRequest passwordChangeRequest = new ChangeLoginRequest();
        passwordChangeRequest.setUsername(username1);
        passwordChangeRequest.setNewPassword(newPassword);
        passwordChangeRequest.setOldPassword(newPassword);
        authSimulation(username1, password1);
        passwordChangeResponse = trainerController.changePassword(passwordChangeRequest);
        if(passwordChangeResponse.getBody().booleanValue()) password1 = newPassword;
    }

    @Then("trainers password change should be successful")
    public void thenPasswordChangeSuccessful() {
        assertEquals(HttpStatus.OK, passwordChangeResponse.getStatusCode());
        assertEquals(Boolean.TRUE, passwordChangeResponse.getBody());
    }

    @Then("trainers password change should be unsuccessful")
    public void thenPasswordChangeUnsuccessful() {
        assertEquals(HttpStatus.BAD_REQUEST, passwordChangeResponse.getStatusCode());
        assertEquals(Boolean.FALSE, passwordChangeResponse.getBody());
    }

    @When("the trainer tries to update their profile with name {string}, surname {string}, specialization {string}")
    public void whenTrainerTriesToUpdateProfile(String name, String surname, String trainingName) {
        UpdateTrainerProfileRequest updateRequest = new UpdateTrainerProfileRequest();
        updateRequest.setUsername(username1);
        updateRequest.setFirstName(name);
        updateRequest.setLastName(surname);
        updateRequest.setSpecialization(TrainingType.TrainingName.valueOf(trainingName));
        authSimulation(username1, password1);
        try {
            profileResponse = trainerController.update(updateRequest);
        } catch (Exception e) {
            profileResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Then("the trainer profile update should be successful with name {string}, surname {string}, specialization {string}")
    public void thenProfileUpdateSuccessful(String name, String surname, String trainingName) {
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode());
        assertEquals(name, profileResponse.getBody().getFirstName());
        assertEquals(surname, profileResponse.getBody().getLastName());
        assertEquals(trainingName, profileResponse.getBody().getSpecialization().toString());
    }

    @Then("the trainer profile update should fail with a {string} error")
    public void thenUpdateUnsuccessfully(String error) {
        assertEquals(error, profileResponse.getStatusCode().toString());
    }

    @When("the trainer tries to select their profile by username")
    public void whenTrainerTriesToSelectProfileByUsername() {
        authSimulation(username1, password1);
        profileResponse = trainerController.selectByUsername(username1);
    }

    @When("the admin tries to select profile by username {string}")
    public void whenAdminTriesToSelectProfileByNotExistingUsername(String username) {
        authSimulationAsAdmin(username1, password1);
        profileResponse = trainerController.selectByUsername(username);
    }

    @Then("the trainer profile selection should be successful")
    public void thenProfileSelectionSuccessful() {
        assertEquals(HttpStatus.OK, profileResponse.getStatusCode());
        assertEquals(username1.split("\\.")[0], Objects.requireNonNull(profileResponse.getBody().getFirstName()));
    }

    @Then("the trainer profile selection should fail with a {string} error")
    public void thenProfileSelectionUnsuccessful(String error) {
        assertEquals(error, profileResponse.getStatusCode().toString());
    }

    @When("the trainer tries to get their trainings list")
    public void whenTrainerTriesToGetTrainingsList() {
        authSimulation(username1, password1);
        trainingsResponse = trainerController.getTrainerTrainingsList(username1, null, null, null);
    }

    @Then("trainers trainings list retrieval should be successful")
    public void thenTrainingsListRetrievalSuccessful() {
        assertEquals(HttpStatus.OK, trainingsResponse.getStatusCode());
        assertNotNull(trainingsResponse.getBody().getTrainings());
    }

    @Given("a registered and activated trainer with username {string}")
    public void givenRegisteredAndActivatedTrainer(String username) {
        authSimulation(username, "password");
        activateDeactivateRequest = new ActivateDeactivateRequest();
        activateDeactivateRequest.setUsername(username);
    }

    @Given("a registered and deactivated trainer with username {string}")
    public void givenRegisteredAndDeactivatedTrainer(String username) {
        authSimulation(username, "password");
        activateDeactivateRequest = new ActivateDeactivateRequest();
        activateDeactivateRequest.setUsername(username);
    }

    @When("tries to activate himself")
    public void tryingToActivateMyself(){
        activateDeactivateRequest.setActive(true);
        activateDeactivateResponce = trainerController.setActive(activateDeactivateRequest);
    }

    @When("tries to deactivate himself")
    public void tryingToDeactivateMyself(){
        activateDeactivateRequest.setActive(false);
        activateDeactivateResponce = trainerController.setActive(activateDeactivateRequest);
    }

    @Then("the trainer deactivation should be successful")
    public void thenTrainerDeactivationSuccessful() {
        assertTrue(activateDeactivateResponce.getBody().booleanValue());
    }

    @Then("the trainer activation should be successful")
    public void thenTrainerActivationSuccessful() {
        assertTrue(activateDeactivateResponce.getBody().booleanValue());
    }


    @When("as admin tries to activate the trainer with username {string}")
    public void whenAdminTriesToActivateTrainer(String username) {
        authSimulationAsAdmin(activateDeactivateRequest.getUsername(), "password");
        activateDeactivateRequest.setUsername(username);
        activateDeactivateRequest.setActive(true);
        activateDeactivateResponce = trainerController.setActive(activateDeactivateRequest);
    }


    @When("as admin tries to deactivate the trainer with username {string}")
    public void whenAdminTriesToDeactivateTrainer(String username) {
        authSimulationAsAdmin(activateDeactivateRequest.getUsername(), "password");
        activateDeactivateRequest.setUsername(username);
        activateDeactivateRequest.setActive(false);
        activateDeactivateResponce = trainerController.setActive(activateDeactivateRequest);
    }

    @Then("the trainer activation should fail with a {string} error")
    public void thenTrainerActivationFails(String error) {
        assertEquals(error, activateDeactivateResponce.getStatusCode().toString());
    }

    @Then("the trainer deactivation should fail with a {string} error")
    public void thenTrainerDeactivationFails(String error) {
        assertEquals(error, activateDeactivateResponce.getStatusCode().toString());
    }


    private void authSimulation(String username, String password) {
        UserDetails userDetails = User.withUsername(username).password(password).roles(Role.of(Role.Authority.TRAINER).toString()).build();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void authSimulationAsAdmin(String username, String password) {
        UserDetails userDetails = User.withUsername(username).password(password).roles(Role.of(Role.Authority.ADMIN).toString()).build();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}

