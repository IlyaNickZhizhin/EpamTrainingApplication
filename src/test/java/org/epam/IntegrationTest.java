//package org.epam;
//
//import org.epam.config.Config;
//import org.epam.dto.traineeDto.TraineeDto;
//import org.epam.dto.trainerDto.TrainerDto;
//import org.epam.dto.trainingDto.TrainingDto;
//import org.epam.mapper.GymGeneralMapper;
//import org.epam.model.gymModel.Trainer;
//import org.epam.model.gymModel.TrainingType;
//import org.epam.service.TraineeService;
//import org.epam.service.TrainerService;
//import org.epam.service.TrainingService;
//import org.epam.testBeans.TestConfig;
//import org.epam.testBeans.TestInitDB;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.List;
//
//
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {TestConfig.class, TestInitDB.class, Config.class})
//@ActiveProfiles("test")
//public class IntegrationTest {
//
//    @Autowired
//    private TraineeService traineeService;
//    @Autowired
//    private TrainerService trainerService;
//    @Autowired
//    private TrainingService trainingService;
//    @Autowired
//    GymGeneralMapper gymGeneralMapper;
//    private Reader reader = new Reader();
//    {
//        reader.setStartPath("src/test/resources/models/");
//        reader.setEndPath(".json");
//    }
//
//    @Test
//    public void traineeTrainerTrainingIntegrationTest() {
//        TrainerDto trainer1 = reader.readDto("trainers/trainer1", reader::readTrainer, gymGeneralMapper::trainerToTrainerDto);
//        TrainerDto trainer2 = reader.readDto("trainers/trainer2", reader::readTrainer, gymGeneralMapper::trainerToTrainerDto);
//        TraineeDto trainee3 = reader.readDto("trainees/trainee1", reader::readTrainee, gymGeneralMapper::traineeToTraineeDto);
//        TraineeDto trainee4 = reader.readDto("trainees/trainee2", reader::readTrainee, gymGeneralMapper::traineeToTraineeDto);
//        TraineeDto trainee5 = reader.readDto("trainees/trainee3", reader::readTrainee, gymGeneralMapper::traineeToTraineeDto);
//        TraineeDto trainee6 = reader.readDto("trainees/trainee4", reader::readTrainee, gymGeneralMapper::traineeToTraineeDto);
//        TrainingDto training1 = reader.readDto("trainings/training1", reader::readTraining, gymGeneralMapper::trainingToTrainingDto);
//        TrainingDto training2 = reader.readDto("trainings/training2", reader::readTraining, gymGeneralMapper::trainingToTrainingDto);
//        TrainingDto training3 = reader.readDto("trainings/training3", reader::readTraining, gymGeneralMapper::trainingToTrainingDto);
//        TrainingDto training4 = reader.readDto("trainings/training4", reader::readTraining, gymGeneralMapper::trainingToTrainingDto);
//        TrainingDto training5 = reader.readDto("trainings/training5", reader::readTraining, gymGeneralMapper::trainingToTrainingDto);
//        TrainingDto training6 = reader.readDto("trainings/training6", reader::readTraining, gymGeneralMapper::trainingToTrainingDto);
//        String p1 = trainerService.create(trainer1).getPassword(); trainer1.setPassword(p1);
//        String p2 = trainerService.create(trainer2).getPassword(); trainer2.setPassword(p2);
//        String p3 = traineeService.create(trainee3).getPassword(); trainee3.setPassword(p3);
//        String p4 = traineeService.create(trainee4).getPassword(); trainee4.setPassword(p4);
//        String p5 = traineeService.create(trainee5).getPassword(); trainee5.setPassword(p5);
//        String p6 = traineeService.create(trainee6).getPassword(); trainee6.setPassword(p6);
//        trainingService.create(trainer1_Username, trainer1_Password, trainee3, "BASIC", trainingType1, traning1_Date, traning1_Duration);
//        trainingService.create(trainer2_Username, trainer2_Password, trainee4, "CARDIO", trainingType2, traning2_Date, traning2_Duration);
//        trainingService.create(trainee4_Username, trainee4_Password, trainer1, "BASIC", trainingType1, traning1_Date, traning1_Duration);
//        trainingService.create(trainee5_Username, trainee5_Password, trainer2, "CARDIO", trainingType2, traning2_Date, traning2_Duration);
//        trainerService.update(user1.getUsername(), user1.getPassword(), trainer1.getId(),
//                new Trainer(new TrainingType(4, WORKOUT), user1));
//
////      trainerService.update(trainer2.getId(),
////                new Trainer(new TrainingType(5, TrainingType.TrainingName.YOGA), user2),  // wrong password exception
////                user2.getUsername(), user1.getPassword());
//        //trainerService.delete(trainer1.getId(), user1.getUsername(), user1.getPassword()); // unsupported operation exception
//        trainerService.select(user1.getUsername(), user1.getPassword(), trainer1.getId())
//                .getSpecialization().getTrainingName();
////      System.out.println(trainerService.select(trainer1.getId(), user2.getUsername(), user2.getPassword())
////                .getSpecialization().getTrainingName()); // wrong password exception
//        trainerService.selectByUsername(user1.getUsername(), user1.getPassword())
//                .getSpecialization().getTrainingName();
//        trainerService.setActive(user1.getUsername(), user1.getPassword(),false);
//        trainerService.selectByUsername(user1.getUsername(), user1.getPassword())
//                .getUser().isActive();
//        trainerService.setActive(user1.getUsername(), user1.getPassword(), false);
//        trainingService.getTraineeTrainingsList(trainee4_Username, trainee4_Password, List.of(trainingType2));
//        trainingService.getTraineeTrainingsList(trainer2_Username, trainer2_Password, List.of(trainingType2));
//        trainingService.getNotAssignedOnTraineeActiveTrainers(trainee4_Username, trainee4_Password);
//        trainingService.getNotAssignedOnTraineeActiveTrainers(trainee6_Username, trainee6_Password);
//        traineeService.delete(trainee4_Username, trainee4_Password, trainee4.getId());
//        traineeService.delete(trainee3_Username, trainee3_Password, trainee3.getId());
//        traineeService.delete(trainee5_Username, trainee5_Password, trainee5.getId());
//        traineeService.delete(trainee6_Username, trainee6_Password, trainee6.getId());
//    }
//}
