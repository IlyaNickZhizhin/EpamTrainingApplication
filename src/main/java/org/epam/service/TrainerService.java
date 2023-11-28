package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.dao.TrainerDaoImpl;
import org.epam.dao.UserDaoImpl;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.LoginRequest;
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
    private final TrainerMapper trainerMapper;
    private final TrainerDaoImpl gymDao;
    private final UserDaoImpl userDao;
    private final GymGeneralService<Trainer> superService;

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
        Trainer trainer = superService.selectByUsername(request.getUsername());
        User user = trainer.getUser();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstname());
        user.setLastName(request.getLastname());
        user.setActive(request.isActive());
        trainer.setUser(user);
        if (request.getSpecialization() != null) trainer.setSpecialization(trainerMapper.stringToTrainingType(request.getSpecialization()));
        trainer = gymDao.update(trainer.getId(), trainer);
        return trainerMapper.trainerToProfileResponse(trainer);
    }
    @Transactional(readOnly = true)
    public TrainerProfileResponse selectByUsername(String username) throws VerificationException {
        Trainer trainer = superService.selectByUsername(username);
        return trainerMapper.trainerToProfileResponse(trainer);
    }

    @Transactional
    public void changePassword(ChangeLoginRequest request) throws VerificationException {
        Trainer trainer = superService.selectByUsername(request.getUsername());
        if (trainer==null) throw new ProhibitedActionException("No one except Trainer could not use TraineeService");
        superService.changePassword(trainer.getUser(), request.getNewPassword());
    }
    @Transactional
    public void setActive(ActivateDeactivateRequest request) throws VerificationException {
        Trainer trainer = superService.selectByUsername(request.getUsername());
        if (trainer==null) throw new ProhibitedActionException("No one except Trainer could not use TraineeService");
        superService.setActive(trainer.getUser(), request.isActive());
    }

    protected String getModelName() {
        return "Trainer";
    }
}
