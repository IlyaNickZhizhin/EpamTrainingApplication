package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.epam.dao.TrainerRepository;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.trainerDto.TrainerProfileResponse;
import org.epam.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.dto.trainerDto.UpdateTrainerProfileRequest;
import org.epam.exceptions.InvalidDataException;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.mapper.TrainerMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerMapper trainerMapper;
    private final TrainerRepository trainerRepository;
    private final UserService userService;

    @Transactional
    public RegistrationResponse create(TrainerRegistrationRequest request) {
        log.info("Creating " + getModelName());
        User user = userService.setNewUser(request.getFirstName(), request.getLastName()).orElseThrow(() -> {
            log.error("Troubles with creating user: " + request.getFirstName() + "." + request.getLastName());
            return new InvalidDataException("userDao.setNewUser(" + request.getFirstName() + ", " + request.getLastName() + ")",
                    "Troubles with creating user: " + request.getFirstName() + " " + request.getLastName());
        });
        log.info("Creating " + getModelName() + " with user: " + request.getFirstName() + "." + request.getLastName());
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(trainerMapper.stringToTrainingType(request.getSpecialization()));
        log.info("Created " + getModelName() + " with id " + trainer.getId());
        return trainerMapper.trainerToRegistrationResponse(trainerRepository.save(trainer));

    }
    @Transactional
    public TrainerProfileResponse update(String username, UpdateTrainerProfileRequest request) {
        ImmutablePair<User, Trainer> pair = getUserTrainer(username);
        pair.left.setUsername(request.getUsername());
        pair.left.setFirstName(request.getFirstName());
        pair.left.setLastName(request.getLastName());
        pair.left.setActive(request.isActive());
        if (request.getSpecialization() != null)
            pair.right.setSpecialization(trainerMapper.stringToTrainingType(request.getSpecialization()));
        pair.right.setUser(userService.update(pair.left.getId(), pair.left)
                .orElseThrow(() -> {
                    log.error("Troubles with updating user " + request.getUsername());
                    return new InvalidDataException("userDao.update(" + pair.left.getId() + ", " + pair.left + ")",
                            "Troubles with updating user " + request.getUsername());
                }));
        return trainerMapper.trainerToProfileResponse(trainerRepository.save(pair.right));
    }
    @Transactional(readOnly = true)
    public TrainerProfileResponse selectByUsername(String username) {
        return trainerMapper.trainerToProfileResponse(getUserTrainer(username).right);
    }

    @Transactional
    public boolean changePassword(ChangeLoginRequest request) {
        ImmutablePair<User, Trainer> pair = getUserTrainer(request.getUsername());
        if (pair.left.getPassword().equals(request.getNewPassword())) throw new ProhibitedActionException("New password is the same as old");
        pair.left.setPassword(request.getNewPassword());
        return userService.update(pair.left.getId(), pair.left)
                .orElseThrow(
                        () -> {
                            log.error("Troubles with updating user " + request.getUsername());
                            return new InvalidDataException("userDao.update(" + pair.left.getId() + ", " + pair.left + ")",
                                    "Troubles with updating user " + request.getUsername());
                        }
                )
                .getPassword().equals(request.getNewPassword());
    }
    @Transactional
    public boolean setActive(ActivateDeactivateRequest request) {
        User user = getUserTrainer(request.getUsername()).left;
        if (user.isActive() != request.isActive()) {
            user.setActive(request.isActive());
            userService.update(user.getId(), user).orElseThrow(() -> {
                log.error("Troubles with updating user " + request.getUsername());
                return new InvalidDataException("userDao.update(" + user.getId() + ", " + user + ")",
                        "Troubles with updating user " + request.getUsername());
            });
        }
        return true;
    }

    protected String getModelName() {
        return "Trainer";
    }

    private ImmutablePair<User, Trainer> getUserTrainer(String username) {
        User user = userService.findByUsername(username).orElseThrow(() -> {
            log.error("No user with username " + username);
            return new InvalidDataException("userDao.getByUsername(" + username + ")", "No user with username " + username);
        });
        Trainer trainer = trainerRepository.findByUser(user).orElseThrow(() -> {
            log.error("No trainee with username " + username);
            return new ProhibitedActionException("No one except trainer could use this service, " +
                    "but there are no trainer with username " + username);
        });
        return new ImmutablePair<>(user,trainer);
    }
}
