package org.epam;

import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;

import java.time.LocalDate;

import static org.epam.model.gymModel.TrainingType.TrainingName.BASIC;
import static org.epam.model.gymModel.TrainingType.TrainingName.CARDIO;

public interface TestDatabaseInitializer {

    String trainer1_FirstName = "Petr"; String trainer1_LastName = "Petrov";  boolean trainer1_Active = true;
    String trainer1_Username = "Petr.Petrov"; String trainer1_Password = "password01";
    String trainer2_FirstName = "Ivan"; String trainer2_LastName = "Ivanov";  boolean trainer2_Active = true;
    String trainer2_Username = "Ivan.Ivanov"; String trainer2_Password = "password02";
    String trainee3_FirstName = "Sidor"; String trainee3_LastName = "Sidorov";  boolean trainee3_Active = true;
    String trainee3_Username = "Sidor.Sidorov"; String trainee3_Password = "password03";
    LocalDate trainee3_Birthday = LocalDate.of(2000, 1, 1); String trainee3_Address = "Tashkent";
    String trainee4_FirstName = "Vasiliy"; String trainee4_LastName = "Vasiliev";  boolean trainee4_Active = true;
    String trainee4_Username = "Vasiliy.Vasiliev"; String trainee4_Password = "password04";
    LocalDate trainee4_Birthday = LocalDate.of(2000, 2, 2); String trainee4_Address = "Samarkand";
    String trainee5_FirstName = "Semen"; String trainee5_LastName = "Semenov";  boolean trainee5_Active = true;
    String trainee5_Username = "Semen.Semenov"; String trainee5_Password = "password05";
    LocalDate trainee5_Birthday = LocalDate.of(2000, 3, 3);
    String trainee6_FirstName = "Pavel"; String trainee6_LastName = "Pavlov";  boolean trainee6_Active = true;
    String trainee6_Username = "Pavel.Pavlov"; String trainee6_Password = "password06";
    String trainee6_Address = "Bukhara";
    LocalDate traning1_Date = LocalDate.of(2023, 11, 15); double traning1_Duration = 1;
    LocalDate traning2_Date = LocalDate.of(2023, 11, 15); double traning2_Duration = 1.5;

    User user1 =
            new User(1, trainer1_FirstName, trainer1_LastName, trainer1_Username, trainer1_Password, trainer1_Active);
    User user2 =
            new User(2, trainer2_FirstName, trainer2_LastName, trainer2_Username, trainer2_Password, trainer2_Active);

    User user3 =
            new User(3, trainee3_FirstName, trainee3_LastName, trainee3_Username, trainee3_Password, trainee3_Active);

    User user4 =
            new User(4, trainee4_FirstName, trainee4_LastName, trainee4_Username, trainee4_Password, trainee4_Active);

    User user5 =
            new User(5, trainee5_FirstName, trainee5_LastName, trainee5_Username, trainee5_Password, trainee5_Active);

    User user6 =
            new User(6, trainee6_FirstName, trainee6_LastName, trainee6_Username, trainee6_Password, trainee6_Active);

    TrainingType.TrainingName trainingName1 = BASIC; TrainingType trainingType1 = new TrainingType(1, trainingName1);
    TrainingType.TrainingName trainingName2 = CARDIO; TrainingType trainingType2 = new TrainingType(2, trainingName2);

    Trainer trainer1 = new Trainer(1, trainingType1, user1);

    Trainer trainer2 = new Trainer(2, trainingType2, user2);

    Trainee trainee3 = new Trainee(1, trainee3_Birthday, trainee3_Address, user3, null);

    Trainee trainee4 = new Trainee(2, trainee4_Birthday, trainee4_Address, user4, null);

    Trainee trainee5 = new Trainee(3, trainee5_Birthday, null, user5, null);

    Trainee trainee6 = new Trainee(4, null, trainee6_Address, user6, null);



    Training training1 = new Training(1, trainee3, trainer1, "BASIC", trainingType1, traning1_Date, traning1_Duration);

    Training training2 = new Training(
            1, trainee4, trainer2, "CARDIO", trainingType2, traning2_Date, traning2_Duration);


}