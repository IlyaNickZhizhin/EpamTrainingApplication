package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.TraineeDaoImpl;
import org.epam.dao.TrainerDaoImpl;
import org.epam.dao.UserDao;
import org.epam.dto.LoginRequest;
import org.epam.exceptions.InvalidDataException;
import org.epam.exceptions.VerificationException;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    private final UserDao userDao;
    private final TraineeDaoImpl traineeDao;
    private final TrainerDaoImpl trainerDao;
    private final PasswordChecker passwordChecker;

    @Transactional(readOnly = true)
    public Object login(LoginRequest request) throws VerificationException, InvalidDataException {
        User user = userDao.getByUsername(request.getUsername()).orElseThrow(() -> new InvalidDataException(LoginService.class
                .getSimpleName()+"login", "username" + request.getUsername() + "was incorrect"));
        boolean check = passwordChecker.checkPassword(request.getUsername(), request.getPassword(), user);
        if (check) {
            Optional<Trainee> trainee = traineeDao.getModelByUserId(user.getId());
            Optional<Trainer> trainer = trainerDao.getModelByUserId(user.getId());
            if (trainee.isPresent()) {
                log.info("User with username: " + request.getUsername() + " logged in as TRAINEE");
                return trainee.get();
            } else if (trainer.isPresent()) {
                log.info("User with username: " + request.getUsername() + " logged in as TRAINER");
                return trainer.get();
            } else {
                throw new InvalidDataException("getModelByUser(" +request.getUsername() + ")",
                        "No trainer or trainee with username: " + request.getUsername() + " found ");
            }
        } else {
            throw new VerificationException("Wrong password");
        }
    }
}
