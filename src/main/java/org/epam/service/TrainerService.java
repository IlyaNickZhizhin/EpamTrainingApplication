package org.epam.service;

import lombok.extern.slf4j.Slf4j;
import org.epam.dao.TrainerDaoImpl;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.LoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.ShotTraineeDto;
import org.epam.dto.trainerDto.TrainerDto;
import org.epam.dto.trainerDto.TrainerProfileResponse;
import org.epam.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.dto.trainerDto.UpdateTrainerProfileRequest;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.exceptions.VerificationException;
import org.epam.mapper.TrainerMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TrainerService extends GymAbstractService<Trainer> {

    @Autowired
    private TrainerMapper trainerMapper;
    @Autowired
    public void setTrainerDao(TrainerDaoImpl trainerDaoImpl) {
        super.gymDao = trainerDaoImpl;
    }

    @Transactional
    public RegistrationResponse create(TrainerRegistrationRequest request) {
        log.info("Creating " + getModelName());
        Trainer trainer = new Trainer();
        User user = userDao.setNewUser(request.getFirstname(), request.getLastname());
        log.info("Creating " + getModelName() + " with user " + request.getFirstname() + " " + request.getLastname());
        trainer.setUser(user);
        trainer.setSpecialization(gymGeneralMapper.stringToTrainingType(request.getSpecialization()));
        log.info("Created " + getModelName() + "and parametrized");
        TrainerDto trainerDto = gymGeneralMapper.trainerToTrainerDto(gymDao.create(trainer));
        return trainerMapper.trainerToRegistrationResponse(trainerDto);

    }
    @Transactional
    public TrainerProfileResponse update(LoginRequest login, UpdateTrainerProfileRequest request) throws VerificationException {
        Trainer trainer = loginAndTakeModel(login);
        TrainerDto newDto = trainerMapper.updateTrainerProfileRequestToTrainerDto(request);
        Trainer updatedTrainer = gymGeneralMapper.trainerDtoToTrainer(newDto);
        updatedTrainer = super.update(trainer.getId(), updatedTrainer);
        newDto = gymGeneralMapper.trainerToTrainerDto(updatedTrainer);
        return getTrainerProfileResponse(trainer, newDto);
    }
    @Transactional(readOnly = true)
    public TrainerProfileResponse select(LoginRequest login, String username) throws VerificationException {
        Trainer trainer = loginAndTakeModel(login);
        TrainerDto dto = gymGeneralMapper.trainerToTrainerDto(trainer);
        return getTrainerProfileResponse(trainer, dto);
    }
    private TrainerProfileResponse getTrainerProfileResponse(Trainer trainer, TrainerDto dto) {
        TrainerProfileResponse response = trainerMapper.trainerDtoToUpdateResponse(dto);
        List<Trainee> trainees = trainer.getTrainings().stream().map(Training::getTrainee).collect(Collectors.toList());
        List<ShotTraineeDto> traineesDto = gymGeneralMapper.traineesDtoToShot(
                gymGeneralMapper.traineesToTraineeDtos(trainees));
        response.setTrainees(traineesDto);
        return response;
    }
    @Transactional(readOnly = true)
    public List<TrainerDto> selectAll(TrainerDto trainerDto) throws VerificationException {
        User user = selectUserByUsername(trainerDto.getUsername());
        super.verify(trainerDto.getUsername(), trainerDto.getPassword(), user);
        List<Trainer> trainers = super.selectAll();
        log.info("Selecting all " + getModelName() + "s");
        return gymGeneralMapper.trainersToTrainerDtos(trainers);
    }
    @Transactional
    public void changePassword(ChangeLoginRequest request) throws VerificationException {
        Trainer trainer = loginAndTakeModel(gymGeneralMapper.changeLoginToLogin(request));
        User user = trainer.getUser();
        if (user.getPassword().equals(request.getNewPassword())) {
            throw new ProhibitedActionException("It is not possible to change password for user it is already ");
        }
        user.setPassword(request.getNewPassword());
        userDao.update(user.getId(), user);
    }
    @Transactional
    public void setActive(LoginRequest login, ActivateDeactivateRequest request) throws VerificationException {
        Trainer trainer = loginAndTakeModel(login);
        if (trainer==null) throw new ProhibitedActionException("No one except Trainer could not use TraineeService");
        super.setActive(trainer.getUser(), request.isActive());
    }
    Trainer loginAndTakeModel(LoginRequest login) throws VerificationException, ProhibitedActionException {
        log.info("Login " + getModelName() + " with username " + login.getUsername());
        User user = selectUserByUsername(login.getUsername());
        log.info("Getting " + getModelName() + "with username" + user.getUsername());
        Trainer trainer = gymDao.getModelByUser(user);
        if (trainer==null) throw new ProhibitedActionException("No one except Trainer could not use TrainerService");
        log.info("Verifying " + getModelName() + " with username " + login.getUsername());
        super.verify(login.getUsername(), login.getPassword(), user);
        return trainer;
    }
    @Override
    protected String getModelName() {
        return "Trainer";
    }
}
