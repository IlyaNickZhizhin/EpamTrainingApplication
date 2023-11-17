package org.epam;

import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;

import java.util.Date;

import static org.epam.model.gymModel.TrainingType.TrainingName.BASIC;
import static org.epam.model.gymModel.TrainingType.TrainingName.CARDIO;

public interface Supplier {
    User user1 =
            new User(1, "Petr", "Petrov", "Petr.Petrov", "password01", true);
    User user2 =
            new User(2, "Ivan", "Ivanov", "Ivan.Ivanov", "password02", true);

    User user3 =
            new User(3, "Sidor", "Sidorov", "Sidor.Sidorov", "password03", true);

    User user4 =
            new User(4, "Vasiliy", "Vasiliev", "Vasiliy.Vasiliev", "password04", true);

    TrainingType trainingType1 = new TrainingType(1, BASIC);

    TrainingType trainingType2 = new TrainingType(2, CARDIO);

    Trainer trainer1 = new Trainer(1, trainingType1, user1);

    Trainer trainer2 = new Trainer(2, trainingType2, user2);

    Trainee trainee1 = new Trainee(1, new Date(2000, 1, 1), "Tashkent", user3);

    Trainee trainee2 = new Trainee(2, new Date(2000, 2, 2), "Samarkand", user4);

    Training training1 = new Training(
            1, trainee1, trainer1, "BASIC", trainingType1, new Date(2023, 11, 15), 1);

    Training training2 = new Training(
            1, trainee2, trainer2, "CARDIO", trainingType2, new Date(2023, 11, 15), 1.5);

}
