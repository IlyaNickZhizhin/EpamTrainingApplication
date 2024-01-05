package org.epam.gymservice.controller;

import org.epam.gymservice.Reader;
import org.epam.gymservice.dto.LoginRequest;
import org.epam.gymservice.exceptions.InvalidDataException;
import org.epam.gymservice.model.User;
import org.epam.gymservice.service.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    @Test
    public void testLogin() {
        Reader reader = new Reader();
        reader.setStartPath("");
        reader.setEndPath("");
        User user = reader.readEntity("src/test/resources/models/users/user1.json", User.class);
        LoginRequest request = new LoginRequest();
        request.setUsername(user.getUsername());
        request.setPassword(user.getPassword());
        when(loginService.login(request)).thenReturn("Authorized");
        assertEquals(new ResponseEntity<>("Authorized", HttpStatus.OK), loginController.login(request));
    }

    @Test
    public void testLoginBadRequest() {
        Reader reader = new Reader();
        reader.setStartPath("");
        reader.setEndPath("");
        User user = reader.readEntity("src/test/resources/models/users/user1.json", User.class);
        LoginRequest request = new LoginRequest();
        request.setUsername(user.getUsername()+1);
        request.setPassword(user.getPassword()+1);
        InvalidDataException ex = new InvalidDataException("1","2");
        when(loginService.login(request)).thenThrow(ex);
        assertEquals(new ResponseEntity<>("Invalid data in method 1: 2", HttpStatus.BAD_REQUEST), loginController.login(request));
    }
}