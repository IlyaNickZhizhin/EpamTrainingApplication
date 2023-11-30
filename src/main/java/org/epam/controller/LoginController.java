package org.epam.controller;

import lombok.RequiredArgsConstructor;
import org.epam.dto.LoginRequest;
import org.epam.exceptions.InvalidDataException;
import org.epam.exceptions.VerificationException;
import org.epam.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        try {
            return new ResponseEntity<>(loginService.login(request), HttpStatus.OK);
        } catch (VerificationException | InvalidDataException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
