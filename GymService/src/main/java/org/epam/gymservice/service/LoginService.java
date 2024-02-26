package org.epam.gymservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.gymservice.dto.LoginRequest;
import org.epam.gymservice.exceptions.InvalidDataException;
import org.epam.gymservice.exceptions.VerificationException;
import org.epam.gymservice.model.User;
import org.epam.gymservice.repository.UserRepository;
import org.epam.gymservice.service.security.JwtService;
import org.epam.gymservice.service.security.LoginAttemptService;
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
        log.info("Checking username and password in " + getClass().getSimpleName());
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
            throw new VerificationException(e.getMessage());
        }
    }
}
