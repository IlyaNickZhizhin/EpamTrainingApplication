package org.epam;

import org.epam.config.Config;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.TrainingType;
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

import java.util.List;

import static org.epam.TestDatabaseInitializer.*;
import static org.epam.model.gymModel.TrainingType.TrainingName.WORKOUT;


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

    @Test
    public void traineeTrainerTrainingIntegrationTest() {
        String p1 = trainerService.create(trainer1_FirstName, trainer1_LastName, trainingType1).getUser().getPassword();
        String p2 = trainerService.create(trainer2_FirstName, trainer2_LastName, trainingType2).getUser().getPassword();
        String p3 = traineeService.create(trainee3_FirstName, trainee3_LastName, trainee3_Address, trainee3_Birthday).getUser().getPassword();
        String p4 = traineeService.create(trainee4_FirstName, trainee4_LastName, trainee3_Address, trainee4_Birthday).getUser().getPassword();
        //traineeService.create(user5.getFirstName(), user5.getLastName(), trainingType1); // invalid data exception
        String p5 = traineeService.create(user5.getFirstName(), user5.getLastName(), trainee5_Birthday).getUser().getPassword();
        String p6 = traineeService.create(user6.getFirstName(), user6.getLastName(), trainee6_Address).getUser().getPassword();
        trainerService.changePassword(user1.getUsername(), p1, trainer1_Password);
        trainerService.changePassword(user2.getUsername(), p2, trainer2_Password);
        traineeService.changePassword(user3.getUsername(), p3, trainee3_Password);
        traineeService.changePassword(user4.getUsername(), p4, trainee4_Password);
        traineeService.changePassword(user5.getUsername(), p5, trainee5_Password);
        traineeService.changePassword(user6.getUsername(), p6, trainee6_Password);
        trainingService.create(trainer1_Username, trainer1_Password, trainee3, "BASIC", trainingType1, traning1_Date, traning1_Duration);
        trainingService.create(trainer2_Username, trainer2_Password, trainee4, "CARDIO", trainingType2, traning2_Date, traning2_Duration);
//      trainingService.create(trainee3_Username, trainee3_Password, trainer1, "BASIC", trainingType1, traning1_Date, traning1_Duration);
//      trainingService.create(trainee4_Username, trainee4_Password, trainer2, "CARDIO", trainingType2, traning2_Date, traning2_Duration);
        trainingService.create(trainee4_Username, trainee4_Password, trainer1, "BASIC", trainingType1, traning1_Date, traning1_Duration);
        trainingService.create(trainee5_Username, trainee5_Password, trainer2, "CARDIO", trainingType2, traning2_Date, traning2_Duration);
        trainerService.update(user1.getUsername(), user1.getPassword(), trainer1.getId(),
                new Trainer(new TrainingType(4, WORKOUT), user1));

//      trainerService.update(trainer2.getId(),
//                new Trainer(new TrainingType(5, TrainingType.TrainingName.YOGA), user2),  // wrong password exception
//                user2.getUsername(), user1.getPassword());
        //trainerService.delete(trainer1.getId(), user1.getUsername(), user1.getPassword()); // unsupported operation exception
        trainerService.select(user1.getUsername(), user1.getPassword(), trainer1.getId())
                .getSpecialization().getTrainingName();
//      System.out.println(trainerService.select(trainer1.getId(), user2.getUsername(), user2.getPassword())
//                .getSpecialization().getTrainingName()); // wrong password exception
        trainerService.selectByUsername(user1.getUsername(), user1.getPassword())
                .getSpecialization().getTrainingName();
        trainerService.setActive(user1.getUsername(), user1.getPassword(),false);
        trainerService.selectByUsername(user1.getUsername(), user1.getPassword())
                .getUser().isActive();
        trainerService.setActive(user1.getUsername(), user1.getPassword(), false);
        trainingService.getTrainingsByUsernameAndTrainingTypes(trainee4_Username, trainee4_Password, List.of(trainingType2));
        trainingService.getTrainingsByUsernameAndTrainingTypes(trainer2_Username, trainer2_Password, List.of(trainingType2));
        trainingService.getAllTrainersAvalibleForTrainee(trainee4_Username, trainee4_Password);
        trainingService.getAllTrainersAvalibleForTrainee(trainee6_Username, trainee6_Password);
        traineeService.delete(trainee4_Username, trainee4_Password, trainee4.getId());
        traineeService.delete(trainee3_Username, trainee3_Password, trainee3.getId());
        traineeService.delete(trainee5_Username, trainee5_Password, trainee5.getId());
        traineeService.delete(trainee6_Username, trainee6_Password, trainee6.getId());
    }
}
