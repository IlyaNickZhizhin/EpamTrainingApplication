package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.epam.dao.TrainerDaoImpl;
import org.epam.dao.UserDao;
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
    private final TrainerDaoImpl gymDao;
    private final UserDao userDao;

    @Transactional
    public RegistrationResponse create(TrainerRegistrationRequest request) {
        log.info("Creating " + getModelName());
        User user = userDao.setNewUser(request.getFirstname(), request.getLastname()).orElseThrow(() -> {
            log.error("Troubles with creating user: " + request.getFirstname() + "." + request.getLastname());
            return new InvalidDataException("userDao.setNewUser(" + request.getFirstname() + ", " + request.getLastname() + ")",
                    "Troubles with creating user: " + request.getFirstname() + " " + request.getLastname());
        });
        log.info("Creating " + getModelName() + " with user: " + request.getFirstname() + "." + request.getLastname());
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(trainerMapper.stringToTrainingType(request.getSpecialization()));
        Trainer createdTrainer = gymDao.create(trainer).orElseThrow(() -> {
            log.error("Troubles with creating " + getModelName() + " with user: " + request.getFirstname() + "." + request.getLastname());
            return new InvalidDataException("gymDao.create(" + trainer + ")",
                    "Troubles with creating " + getModelName() + " with user: " + request.getFirstname() + "." + request.getLastname());
        });
        log.info("Created " + getModelName() + " with id " + trainer.getId());
        return trainerMapper.trainerToRegistrationResponse(createdTrainer);

    }
    @Transactional
    public TrainerProfileResponse update(UpdateTrainerProfileRequest request) {
        ImmutablePair<User, Trainer> pair = getUserTrainer(request.getUsername());
        pair.left.setUsername(request.getUsername());
        pair.left.setFirstName(request.getFirstname());
        pair.left.setLastName(request.getLastname());
        pair.left.setActive(request.isActive());
        if (request.getSpecialization() != null)
            pair.right.setSpecialization(trainerMapper.stringToTrainingType(request.getSpecialization()));
        pair.right.setUser(userDao.update(pair.left.getId(), pair.left)
                .orElseThrow(() -> {
                    log.error("Troubles with updating user " + request.getUsername());
                    return new InvalidDataException("userDao.update(" + pair.left.getId() + ", " + pair.left + ")",
                            "Troubles with updating user " + request.getUsername());
                }));
        Trainer updatedTrainer = gymDao.update(pair.right.getId(), pair.right).orElseThrow(
                () -> {
                    log.error("Troubles with updating " + getModelName() + " " + request.getUsername());
                    return new InvalidDataException("gymDao.update(" + pair.right.getId() + ", " + pair.right + ")",
                            "Troubles with updating " + getModelName() + " " + request.getUsername());
                });
        return trainerMapper.trainerToProfileResponse(updatedTrainer);
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
        return userDao.update(pair.left.getId(), pair.left)
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
        ImmutablePair<User, Trainer> pair = getUserTrainer(request.getUsername());
        if (pair.left.isActive() != request.isActive()) userDao.update(pair.left.getId(), pair.left).orElseThrow(() -> {
            log.error("Troubles with updating user " + request.getUsername());
            return new InvalidDataException("userDao.update(" + pair.left.getId() + ", " + pair.left + ")",
                    "Troubles with updating user " + request.getUsername());
        });
        return true;
    }

    protected String getModelName() {
        return "Trainer";
    }

    private ImmutablePair<User, Trainer> getUserTrainer(String username) {
        User user = userDao.getByUsername(username).orElseThrow(() -> {
            log.error("No user with username " + username);
            return new InvalidDataException("userDao.getByUsername(" + username + ")", "No user with username " + username);
        });
        Trainer trainer = gymDao.getModelByUser(user).orElseThrow(() -> {
            log.error("No trainee with username " + username);
            return new ProhibitedActionException("No one except trainer could use this service, " +
                    "but there are no trainer with username " + username);
        });
        return new ImmutablePair<>(user,trainer);
    }
}
