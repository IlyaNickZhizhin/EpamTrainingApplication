package org.epam;

import org.epam.config.Config;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.LoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.dto.trainerDto.TrainerRegistrationRequest;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


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
    private TestMapper testMapper = TestMapper.INSTANCE;

    private Reader reader = new Reader();
    {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
    }

    @Test
    public void traineeTrainerTrainingIntegrationTest() {
        TrainerRegistrationRequest TrRQ1 = reader.readDto("trainers/trainer1", Trainer.class, testMapper::trainerToRegistrationRequest);
        TrainerRegistrationRequest TrRQ2 = reader.readDto("trainers/trainer2", Trainer.class, testMapper::trainerToRegistrationRequest);
        TraineeRegistrationRequest TeRQ3 = reader.readDto("trainees/trainee3", Trainee.class, testMapper::traineeToRegistrationRequest);
        TraineeRegistrationRequest TeRQ4 = reader.readDto("trainees/trainee4", Trainee.class, testMapper::traineeToRegistrationRequest);
        TraineeRegistrationRequest TeRQ5 = reader.readDto("trainees/trainee5", Trainee.class, testMapper::traineeToRegistrationRequest);
        TraineeRegistrationRequest TeRQ6 = reader.readDto("trainees/trainee6", Trainee.class, testMapper::traineeToRegistrationRequest);
        RegistrationResponse TrRR1 = trainerService.create(TrRQ1);
        RegistrationResponse TrRR2 = trainerService.create(TrRQ2);
        RegistrationResponse TeRR3 = traineeService.create(TeRQ3);
        RegistrationResponse TeRR4 = traineeService.create(TeRQ4);
        RegistrationResponse TeRR5 = traineeService.create(TeRQ5);
        RegistrationResponse TeRR6 = traineeService.create(TeRQ6);
        LoginRequest TrLQ1 = reader.readDto("users/user1", User.class, testMapper::userToLoginRequest);
        TrLQ1.setPassword(TrRR1.getPassword());
        LoginRequest TrLQ2 = reader.readDto("users/user2", User.class, testMapper::userToLoginRequest);
        TrLQ2.setPassword(TrRR2.getPassword());
        LoginRequest TeLQ3 = reader.readDto("users/user3", User.class, testMapper::userToLoginRequest);
        TeLQ3.setPassword(TeRR3.getPassword());
        LoginRequest TeLQ4 = reader.readDto("users/user4", User.class, testMapper::userToLoginRequest);
        TeLQ4.setPassword(TeRR4.getPassword());
        LoginRequest TeLQ5 = reader.readDto("users/user5", User.class, testMapper::userToLoginRequest);
        TeLQ5.setPassword(TeRR5.getPassword());
        LoginRequest TeLQ6 = reader.readDto("users/user6", User.class, testMapper::userToLoginRequest);
        TeLQ6.setPassword(TeRR6.getPassword());
        Trainer trainer1 = (Trainer) loginService.login(TrLQ1);
        Trainer trainer2 = (Trainer) loginService.login(TrLQ2);
        Trainee trainee3 = (Trainee) loginService.login(TeLQ3);
        Trainee trainee4 = (Trainee) loginService.login(TeLQ4);
        Trainee trainee5 = (Trainee) loginService.login(TeLQ5);
        Trainee trainee6 = (Trainee) loginService.login(TeLQ6);
        ChangeLoginRequest TrCLQ1 = testMapper.roleToChangeLoginRequest(trainer1);
        TrCLQ1.setNewPassword("password01");
        ChangeLoginRequest TrCLQ2 = testMapper.roleToChangeLoginRequest(trainer2);
        TrCLQ2.setNewPassword("password02");
        ChangeLoginRequest TeCLQ3 = testMapper.roleToChangeLoginRequest(trainee3);
        TeCLQ3.setNewPassword("password03");
        ChangeLoginRequest TeCLQ4 = testMapper.roleToChangeLoginRequest(trainee4);
        TeCLQ4.setNewPassword("password04");
        ChangeLoginRequest TeCLQ5 = testMapper.roleToChangeLoginRequest(trainee5);
        TeCLQ5.setNewPassword("password05");
        ChangeLoginRequest TeCLQ6 = testMapper.roleToChangeLoginRequest(trainee6);
        TeCLQ6.setNewPassword("password06");
        assertEquals(true, trainerService.changePassword(TrCLQ1));
        assertEquals(true, trainerService.changePassword(TrCLQ2));
        assertEquals(true, traineeService.changePassword(TeCLQ3));
        assertEquals(true, traineeService.changePassword(TeCLQ4));
        assertEquals(true, traineeService.changePassword(TeCLQ5));
        assertEquals(true, traineeService.changePassword(TeCLQ6));
    }
}
