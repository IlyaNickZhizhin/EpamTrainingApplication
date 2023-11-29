package org.epam;

import org.epam.config.Config;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.LoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.mapper.TraineeMapper;
import org.epam.mapper.TrainerMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.service.LoginService;
import org.epam.service.TraineeService;
import org.epam.service.TrainerService;
import org.epam.service.TrainingService;
import org.epam.testBeans.TestConfig;
import org.epam.testBeans.TestInitDB;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, TestInitDB.class, Config.class})
@ActiveProfiles("test")
public class IntegrationTest {

    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private LoginService loginService;
    private final TraineeMapper traineeMapper = Mappers.getMapper(TraineeMapper.class);
    private final TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);

    private final Reader reader = new Reader();
    {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
    }

    @Test
    public void traineeTrainerTrainingIntegrationTest() {
        TrainerRegistrationRequest trainerRegistrationRequest1
                = reader.readDto("trainers/trainer1", Trainer.class, trainerMapper::trainerToRegistrationRequest);
        TrainerRegistrationRequest trainerRegistrationRequest2
                = reader.readDto("trainers/trainer2", Trainer.class, trainerMapper::trainerToRegistrationRequest);
        TraineeRegistrationRequest traineeRegistrationRequest3
                = reader.readDto("trainees/trainee3", Trainee.class, traineeMapper::traineeToRegistrationRequest);
        TraineeRegistrationRequest traineeRegistrationRequest4
                = reader.readDto("trainees/trainee4", Trainee.class, traineeMapper::traineeToRegistrationRequest);
        TraineeRegistrationRequest traineeRegistrationRequest5
                = reader.readDto("trainees/trainee5", Trainee.class, traineeMapper::traineeToRegistrationRequest);
        TraineeRegistrationRequest traineeRegistrationRequest6
                = reader.readDto("trainees/trainee6", Trainee.class, traineeMapper::traineeToRegistrationRequest);
        RegistrationResponse trainerRegistrationResponse1 = trainerService.create(trainerRegistrationRequest1);
        RegistrationResponse trainerRegistrationResponse2 = trainerService.create(trainerRegistrationRequest2);
        RegistrationResponse traineeRegistrationResponse3 = traineeService.create(traineeRegistrationRequest3);
        RegistrationResponse traineeRegistrationResponse4 = traineeService.create(traineeRegistrationRequest4);
        RegistrationResponse traineeRegistrationResponse5 = traineeService.create(traineeRegistrationRequest5);
        RegistrationResponse traineeRegistrationResponse6 = traineeService.create(traineeRegistrationRequest6);
        LoginRequest trainerLoginRequest1
                = reader.readDto("users/user1", User.class, traineeMapper::userToLoginRequest);
        trainerLoginRequest1.setPassword(trainerRegistrationResponse1.getPassword());
        LoginRequest trainerLoginRequest2
                = reader.readDto("users/user2", User.class, traineeMapper::userToLoginRequest);
        trainerLoginRequest2.setPassword(trainerRegistrationResponse2.getPassword());
        LoginRequest traineeLoginRequest3
                = reader.readDto("users/user3", User.class, traineeMapper::userToLoginRequest);
        traineeLoginRequest3.setPassword(traineeRegistrationResponse3.getPassword());
        LoginRequest traineeLoginRequest4
                = reader.readDto("users/user4", User.class, traineeMapper::userToLoginRequest);
        traineeLoginRequest4.setPassword(traineeRegistrationResponse4.getPassword());
        LoginRequest traineeLoginRequest5
                = reader.readDto("users/user5", User.class, traineeMapper::userToLoginRequest);
        traineeLoginRequest5.setPassword(traineeRegistrationResponse5.getPassword());
        LoginRequest traineeLoginRequest6
                = reader.readDto("users/user6", User.class, traineeMapper::userToLoginRequest);
        traineeLoginRequest6.setPassword(traineeRegistrationResponse6.getPassword());
        Trainer trainer1 = (Trainer) loginService.login(trainerLoginRequest1);
        Trainer trainer2 = (Trainer) loginService.login(trainerLoginRequest2);
        Trainee trainee3 = (Trainee) loginService.login(traineeLoginRequest3);
        Trainee trainee4 = (Trainee) loginService.login(traineeLoginRequest4);
        Trainee trainee5 = (Trainee) loginService.login(traineeLoginRequest5);
        Trainee trainee6 = (Trainee) loginService.login(traineeLoginRequest6);
        ChangeLoginRequest trainerChangeLoginRequest1 = traineeMapper.roleToChangeLoginRequest(trainer1);
        trainerChangeLoginRequest1.setNewPassword("password01");
        ChangeLoginRequest trainerChangeLoginRequest2 = traineeMapper.roleToChangeLoginRequest(trainer2);
        trainerChangeLoginRequest2.setNewPassword("password02");
        ChangeLoginRequest traineeChangeLoginRequest3 = traineeMapper.roleToChangeLoginRequest(trainee3);
        traineeChangeLoginRequest3.setNewPassword("password03");
        ChangeLoginRequest traineeChangeLoginRequest4 = traineeMapper.roleToChangeLoginRequest(trainee4);
        traineeChangeLoginRequest4.setNewPassword("password04");
        ChangeLoginRequest traineeChangeLoginRequest5 = traineeMapper.roleToChangeLoginRequest(trainee5);
        traineeChangeLoginRequest5.setNewPassword("password05");
        ChangeLoginRequest traineeChangeLoginRequest6 = traineeMapper.roleToChangeLoginRequest(trainee6);
        traineeChangeLoginRequest6.setNewPassword("password06");
        assertTrue(trainerService.changePassword(trainerChangeLoginRequest1));
        assertTrue(trainerService.changePassword(trainerChangeLoginRequest2));
        assertTrue(traineeService.changePassword(traineeChangeLoginRequest3));
        assertTrue(traineeService.changePassword(traineeChangeLoginRequest4));
        assertTrue(traineeService.changePassword(traineeChangeLoginRequest5));
        assertTrue(traineeService.changePassword(traineeChangeLoginRequest6));
    }
}
