package org.epam.controller;

import org.epam.Reader;
import org.epam.dto.LoginRequest;
import org.epam.exceptions.InvalidDataException;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.epam.service.LoginService;
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
        Trainer trainer = reader.readEntity("src/test/resources/models/trainers/trainer1.json", Trainer.class);
        LoginRequest request = new LoginRequest();
        request.setUsername(user.getUsername());
        request.setPassword(user.getPassword());
        when(loginService.login(request)).thenReturn(trainer);
        assertEquals(new ResponseEntity<>(loginService.login(request), HttpStatus.OK), loginController.login(request));
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