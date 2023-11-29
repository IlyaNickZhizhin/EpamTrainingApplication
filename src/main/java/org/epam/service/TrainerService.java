package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.dao.TrainerDaoImpl;
import org.epam.dao.UserDaoImpl;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.trainerDto.TrainerProfileResponse;
import org.epam.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.dto.trainerDto.UpdateTrainerProfileRequest;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.exceptions.VerificationException;
import org.epam.mapper.TrainerMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerMapper trainerMapper = TrainerMapper.INSTANCE;
    private final TrainerDaoImpl gymDao;
    private final UserDaoImpl userDao;

    @Transactional
    public RegistrationResponse create(TrainerRegistrationRequest request) {
        log.info("Creating " + getModelName());
        User user = userDao.setNewUser(request.getFirstname(), request.getLastname());
        log.info("Creating " + getModelName() + " with user " + request.getFirstname() + " " + request.getLastname());
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(trainerMapper.stringToTrainingType(request.getSpecialization()));
        trainer = gymDao.create(trainer);
        log.info("Created " + getModelName() + " with id " + trainer.getId());
        return trainerMapper.trainerToRegistrationResponse(trainer);

    }
    @Transactional
    public TrainerProfileResponse update(UpdateTrainerProfileRequest request) throws VerificationException {
        User user = userDao.getByUsername(request.getUsername());
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstname());
        user.setLastName(request.getLastname());
        user.setActive(request.isActive());
        Trainer trainer = gymDao.getModelByUser(user);
        if (trainer == null)
            throw new ProhibitedActionException("No one except Trainer could not use TrainerService");
        if (request.getSpecialization() != null)
            trainer.setSpecialization(trainerMapper.stringToTrainingType(request.getSpecialization()));
        trainer = gymDao.update(trainer.getId(), trainer);
        return trainerMapper.trainerToProfileResponse(trainer);
    }
    @Transactional(readOnly = true)
    public TrainerProfileResponse selectByUsername(String username) throws VerificationException {
        User user = userDao.getByUsername(username);
        Trainer trainer = gymDao.getModelByUser(user);
        if (trainer == null)
            throw new ProhibitedActionException("No one except Trainer could not use TrainerService");
        return trainerMapper.trainerToProfileResponse(trainer);
    }

    @Transactional
    public boolean changePassword(ChangeLoginRequest request) throws VerificationException {
        User user = userDao.getByUsername(request.getUsername());
        Trainer trainer = gymDao.getModelByUser(user);
        if (trainer == null)
            throw new ProhibitedActionException("No one except Trainer could not use TrainerService");
        if (user.getPassword().equals(request.getNewPassword())) throw new ProhibitedActionException("New password is the same as old");
        user.setPassword(request.getNewPassword());
        return userDao.update(user.getId(), user).getPassword().equals(request.getNewPassword());
    }
    @Transactional
    public boolean setActive(ActivateDeactivateRequest request) throws VerificationException {
        User user = userDao.getByUsername(request.getUsername());
        Trainer trainer = gymDao.getModelByUser(user);
        if (trainer == null)
            throw new ProhibitedActionException("No one except Trainer could not use TrainerService");
        if (user.isActive() != request.isActive()) userDao.update(user.getId(), user);
        return user.isActive();
    }

    protected String getModelName() {
        return "Trainer";
    }
}
