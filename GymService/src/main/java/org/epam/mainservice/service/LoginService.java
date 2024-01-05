package org.epam.mainservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.mainservice.dto.LoginRequest;
import org.epam.mainservice.exceptions.InvalidDataException;
import org.epam.mainservice.exceptions.VerificationException;
import org.epam.mainservice.model.User;
import org.epam.mainservice.repository.UserRepository;
import org.epam.mainservice.service.security.JwtService;
import org.epam.mainservice.service.security.LoginAttemptService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final LoginAttemptService loginAttemptService;

    @Transactional(readOnly = true)
    public String login(LoginRequest request) throws VerificationException, InvalidDataException {
        log.info("Checking username and password in" + getClass().getSimpleName());
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new InvalidDataException(LoginService.class
                .getSimpleName()+".login", "username" + request.getUsername() + "was incorrect"));
        log.info("User with id: " + user.getId() + " found");
        if (loginAttemptService.isBlocked(user.getUsername())) {
            return "Wait for 5 minutes";
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            log.info("Password for user with id: " + user.getId() + " was correct");
            return jwtService.generateToken(user);
        } catch (BadCredentialsException e) {
            log.error("Password for user with id: " + user.getId() + " was incorrect");
            loginAttemptService.loginFailed(user.getUsername());
            return e.getMessage();
        }
    }
}