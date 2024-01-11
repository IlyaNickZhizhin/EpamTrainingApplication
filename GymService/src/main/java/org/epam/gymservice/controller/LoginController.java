package org.epam.gymservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.gymservice.dto.LoginRequest;
import org.epam.gymservice.exceptions.InvalidDataException;
import org.epam.gymservice.exceptions.VerificationException;
import org.epam.gymservice.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
@Tag(name = "Login controller", description = "for checking password adg getting principal entity")
@Slf4j
@CrossOrigin
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    @Operation(summary = "checks username and password")
    @SecurityRequirements
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        log.info("Checking username and password in" +getClass().getSimpleName());
        try {
            return new ResponseEntity<>(loginService.login(request), HttpStatus.OK);
        } catch (VerificationException | InvalidDataException e) {
            log.error("Error while checking username and password", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout the current user")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, null);
        return ResponseEntity.ok().build();
    }
}
