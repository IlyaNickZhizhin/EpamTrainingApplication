package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.config.security.PasswordChecker;
import org.epam.dto.LoginRequest;
import org.epam.exceptions.InvalidDataException;
import org.epam.exceptions.VerificationException;
import org.epam.model.User;
import org.epam.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordChecker passwordChecker;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public String login(LoginRequest request) throws VerificationException, InvalidDataException {
        log.info("Checking username and password in" + getClass().getSimpleName());
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new InvalidDataException(LoginService.class
                .getSimpleName()+".login", "username" + request.getUsername() + "was incorrect"));
        log.info("User with id: " + user.getId() + " found");
        boolean check = passwordChecker.checkPassword(request.getUsername(), request.getPassword(), user);
        if (check) {
            log.info("Password for user with id: " + user.getId() + " was correct");
            return jwtService.generateToken(user);
        } else {
            log.error("Password for user with id: " + user.getId() + " was incorrect");
            return "Not authorized";
        }
    }
}
