package org.epam.mainservice;

import org.epam.mainservice.dto.ChangeLoginRequest;
import org.epam.mainservice.dto.LoginRequest;
import org.epam.mainservice.dto.RegistrationResponse;
import org.epam.mainservice.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.mainservice.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.mainservice.mapper.TraineeMapper;
import org.epam.mainservice.mapper.TrainerMapper;
import org.epam.mainservice.model.User;
import org.epam.mainservice.model.gymModel.Trainee;
import org.epam.mainservice.model.gymModel.Trainer;
import org.epam.mainservice.service.LoginService;
import org.epam.mainservice.service.TraineeService;
import org.epam.mainservice.service.TrainerService;
import org.epam.mainservice.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
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
        Trainer trainer1 = reader.readEntity("trainers/trainer1", Trainer.class);
        Trainer trainer2 = reader.readEntity("trainers/trainer2", Trainer.class);
        Trainee trainee3 = reader.readEntity("trainees/trainee3", Trainee.class);
        Trainee trainee4 = reader.readEntity("trainees/trainee4", Trainee.class);
        Trainee trainee5 = reader.readEntity("trainees/trainee5", Trainee.class);
        Trainee trainee6 = reader.readEntity("trainees/trainee6", Trainee.class);
        ChangeLoginRequest trainerChangeLoginRequest1 = trainerMapper.trainerToChangeLoginRequest(trainer1);
        trainerChangeLoginRequest1.setOldPassword(trainerRegistrationResponse1.getPassword());
        trainerChangeLoginRequest1.setNewPassword("password01");
        ChangeLoginRequest trainerChangeLoginRequest2 = trainerMapper.trainerToChangeLoginRequest(trainer2);
        trainerChangeLoginRequest2.setOldPassword(trainerRegistrationResponse2.getPassword());
        trainerChangeLoginRequest2.setNewPassword("password02");
        ChangeLoginRequest traineeChangeLoginRequest3 = traineeMapper.traineeToChangeLoginRequest(trainee3);
        traineeChangeLoginRequest3.setOldPassword(traineeRegistrationResponse3.getPassword());
        traineeChangeLoginRequest3.setNewPassword("password03");
        ChangeLoginRequest traineeChangeLoginRequest4 = traineeMapper.traineeToChangeLoginRequest(trainee4);
        traineeChangeLoginRequest4.setOldPassword(traineeRegistrationResponse4.getPassword());
        traineeChangeLoginRequest4.setNewPassword("password04");
        ChangeLoginRequest traineeChangeLoginRequest5 = traineeMapper.traineeToChangeLoginRequest(trainee5);
        traineeChangeLoginRequest5.setOldPassword(traineeRegistrationResponse5.getPassword());
        traineeChangeLoginRequest5.setNewPassword("password05");
        ChangeLoginRequest traineeChangeLoginRequest6 = traineeMapper.traineeToChangeLoginRequest(trainee6);
        traineeChangeLoginRequest6.setOldPassword(traineeRegistrationResponse6.getPassword());
        traineeChangeLoginRequest6.setNewPassword("password06");
        assertTrue(trainerService.changePassword(trainerChangeLoginRequest1));
        assertTrue(trainerService.changePassword(trainerChangeLoginRequest2));
        assertTrue(traineeService.changePassword(traineeChangeLoginRequest3));
        assertTrue(traineeService.changePassword(traineeChangeLoginRequest4));
        assertTrue(traineeService.changePassword(traineeChangeLoginRequest5));
        assertTrue(traineeService.changePassword(traineeChangeLoginRequest6));
    }
}
