package org.epam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.dto.LoginRequest;
import org.epam.exceptions.InvalidDataException;
import org.epam.exceptions.VerificationException;
import org.epam.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
@Tag(name = "Login controller", description = "for checking password adg getting principal entity")
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    @Operation(summary = "checks username and password")
    @SecurityRequirements
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        log.info("Checking username and password in" +getClass().getSimpleName());
        try {
            return new ResponseEntity<>(loginService.login(request), HttpStatus.OK);
        } catch (VerificationException | InvalidDataException e) {
            log.error("Error while checking username and password", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
