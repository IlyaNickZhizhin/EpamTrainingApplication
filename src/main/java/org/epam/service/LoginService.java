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
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public Object login(LoginRequest request) throws VerificationException, InvalidDataException {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new InvalidDataException(LoginService.class
                .getSimpleName()+"login", "username" + request.getUsername() + "was incorrect"));
        boolean check = passwordChecker.checkPassword(request.getUsername(), request.getPassword(), user);
        if (check) {
            Optional<Trainee> trainee = traineeRepository.findByUser(user);
            Optional<Trainer> trainer = trainerRepository.findByUser(user);
            if (trainee.isPresent()) {
                log.info("User with username: " + request.getUsername() + " logged in as TRAINEE");
                return traineeMapper.traineeToShortTraineeDto(trainee.get());
            } else if (trainer.isPresent()) {
                log.info("User with username: " + request.getUsername() + " logged in as TRAINER");
                return trainerMapper.trainerToShortTrainerDto(trainer.get());
            } else {
                throw new InvalidDataException("getModelByUser(" +request.getUsername() + ")",
                        "No trainer or trainee with username: " + request.getUsername() + " found ");
            }
        } else {
            throw new VerificationException("Wrong password");
        }
    }
}
