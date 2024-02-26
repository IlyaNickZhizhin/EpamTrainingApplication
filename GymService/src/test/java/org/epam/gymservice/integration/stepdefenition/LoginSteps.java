package org.epam.gymservice.integration.stepdefenition;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.epam.gymservice.controller.LoginController;
import org.epam.gymservice.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yaml")
public class LoginSteps {

    @Autowired
    private LoginController loginController;

    private LoginRequest request;
    private ResponseEntity<String> response;

    @Given("a user with username {string} and password {string}")
    public void givenUser(String username, String password) {
        request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);
    }

    @When("the user tries to login")
    public void whenUserTriesToLogin() {
        response = loginController.login(request);
    }

    @Then("the login should be successful")
    public void thenLoginSuccessful() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Then("the login should be unsuccessful")
    public void thenLoginUnsuccessful() {
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
