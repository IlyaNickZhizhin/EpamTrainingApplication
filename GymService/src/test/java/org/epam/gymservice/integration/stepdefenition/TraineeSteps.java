package org.epam.gymservice.integration.stepdefenition;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.epam.gymservice.controller.LoginController;
import org.epam.gymservice.controller.TraineeController;
import org.epam.gymservice.dto.ChangeLoginRequest;
import org.epam.gymservice.dto.RegistrationResponse;
import org.epam.gymservice.dto.traineeDto.TraineeRegistrationRequest;
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
import static org.mockito.Mockito.*;

@CucumberContextConfiguration
@SpringBootTest
public class TraineeSteps {

    @Autowired
    private TraineeController traineeController;
    @Autowired
    private LoginController loginController;
    @MockBean
    private static JmsTemplate jmsTemplate;

    private static ResponseEntity<RegistrationResponse> firstRegistrationResponse;
    private static ResponseEntity<RegistrationResponse> secondRegistrationResponse;
    private static ChangeLoginRequest passwordChangeRequest;
    private ResponseEntity<RegistrationResponse> registrationResponse;
    private ResponseEntity<Boolean> passwordChangeResponse;
    private TraineeRegistrationRequest registrationRequest;

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
        if (firstRegistrationResponse == null) firstRegistrationResponse=registrationResponse;
        else secondRegistrationResponse = registrationResponse;
    }

    @Given("a registered trainee with username from previous test")
    public void givenRegisteredTrainee() {
        passwordChangeRequest = new ChangeLoginRequest();
        passwordChangeRequest.setUsername(secondRegistrationResponse.getBody().getUsername());
        passwordChangeRequest.setOldPassword(secondRegistrationResponse.getBody().getPassword());
    }

    @When("the trainee tries to change their password to {string}")
    public void whenTraineeTriesToChangePassword(String newPassword) {
        UserDetails userDetails = User
                .withUsername(passwordChangeRequest.getUsername())
                .password(passwordChangeRequest.getOldPassword())
                .roles(Role.of(Role.Authority.ROLE_TRAINER).toString())
                .build();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        passwordChangeRequest.setNewPassword(newPassword);
        passwordChangeResponse = traineeController.changePassword(passwordChangeRequest);
    }

    @Then("the password change should be successful")
    public void thenPasswordChangeSuccessful() {
        assertEquals(HttpStatus.OK, passwordChangeResponse.getStatusCode());
        assertEquals(Boolean.TRUE, passwordChangeResponse.getBody());
    }
}
