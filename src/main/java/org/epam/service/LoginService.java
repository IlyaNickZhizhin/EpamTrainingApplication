package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.TraineeRepository;
import org.epam.dao.TrainerRepository;
import org.epam.dao.UserRepository;
import org.epam.dto.LoginRequest;
import org.epam.exceptions.InvalidDataException;
import org.epam.exceptions.VerificationException;
import org.epam.mapper.TraineeMapper;
import org.epam.mapper.TrainerMapper;
import org.epam.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final PasswordChecker passwordChecker;
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;

    @Transactional(readOnly = true)
    public String login(LoginRequest request) throws VerificationException, InvalidDataException {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new InvalidDataException(LoginService.class
                .getSimpleName()+"login", "username" + request.getUsername() + "was incorrect"));
        boolean check = passwordChecker.checkPassword(request.getUsername(), request.getPassword(), user);
        if (check) {
            return "Authorized";
        } else {
            return "Not authorized";
        }
    }
}
