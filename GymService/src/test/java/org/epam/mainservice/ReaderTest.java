package org.epam.mainservice;

import org.epam.mainservice.model.User;
import org.epam.mainservice.model.gymModel.Trainee;
import org.epam.mainservice.model.gymModel.Trainer;
import org.epam.mainservice.model.gymModel.Training;
import org.epam.mainservice.model.gymModel.TrainingType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ReaderTest {
    @Test
    public void testReadEntity() {
        Reader reader = new Reader();
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        List<Trainee> trainees = new ArrayList<>(4);
        List<Trainer> trainers = new ArrayList<>(2);
        List<Training> trainings = new ArrayList<>(6);
        List<User> users = new ArrayList<>(6);
        List<TrainingType> trainingTypes = new ArrayList<>(12);
        for (int i = 3; i <= 6; i++) {
            Trainee trainee = reader.readEntity("trainees/trainee" + i, Trainee.class);
            trainees.add(trainee);
        }
        for (int i = 1; i <= 2; i++) {
            Trainer trainer = reader.readEntity("trainers/trainer" + i, Trainer.class);
            trainers.add(trainer);
        }
        for (int i = 1; i <= 6; i++) {
            Training training = reader.readEntity("trainings/training" + i, Training.class);
            trainings.add(training);
        }
        for (int i = 1; i <= 6; i++) {
            User user = reader.readEntity("users/user" + i, User.class);
            users.add(user);
        }
        for (int i = 1; i <= 12; i++) {
            TrainingType trainingType = reader.readEntity("trainingtypes/trainingType" + i, TrainingType.class);
            trainingTypes.add(trainingType);
        }
        assertEquals(4, trainees.size());
        assertEquals(2, trainers.size());
        assertEquals(6, trainings.size());
        assertEquals(6, users.size());
        assertEquals(12, trainingTypes.size());
        assertEquals(trainers.get(0).getUser(), users.get(0));
        assertEquals(trainers.get(1).getUser(), users.get(1));
        assertEquals(trainers.get(0).getSpecialization(), trainingTypes.get(0));
        assertEquals(trainers.get(1).getSpecialization(), trainingTypes.get(1));

        assertEquals(trainees.get(0).getUser(), users.get(2));
        assertEquals(trainees.get(0).getDateOfBirth(), LocalDate.of(2000, 1, 1));
        assertEquals(trainees.get(0).getAddress(), "Tashkent");

        assertEquals(trainees.get(1).getUser(), users.get(3));
        assertEquals(trainees.get(1).getDateOfBirth(), LocalDate.of(2000, 2, 2));
        assertEquals(trainees.get(1).getAddress(), "Samarkand");

        assertEquals(trainees.get(2).getUser(), users.get(4));
        assertEquals(trainees.get(2).getDateOfBirth(), LocalDate.of(2002, 3, 3));
        assertNull(trainees.get(2).getAddress());

        assertEquals(trainees.get(3).getUser(), users.get(5));
        assertNull(trainees.get(3).getDateOfBirth());
        assertEquals(trainees.get(3).getAddress(), "Bukhara");

        assertEquals(trainings.get(0).getTrainer(), trainers.get(0));
        assertEquals(trainings.get(1).getTrainer(), trainers.get(1));
        assertEquals(trainings.get(2).getTrainer(), trainers.get(0));
        assertEquals(trainings.get(3).getTrainer(), trainers.get(1));
        assertEquals(trainings.get(4).getTrainer(), trainers.get(0));
        assertEquals(trainings.get(5).getTrainer(), trainers.get(1));
        assertEquals(trainings.get(0).getTrainee(), trainees.get(0));
        assertEquals(trainings.get(1).getTrainee(), trainees.get(0));
        assertEquals(trainings.get(2).getTrainee(), trainees.get(1));
        assertEquals(trainings.get(3).getTrainee(), trainees.get(1));
        assertEquals(trainings.get(4).getTrainee(), trainees.get(2));
        assertEquals(trainings.get(5).getTrainee(), trainees.get(3));
        assertEquals(trainings.get(0).getTrainingType(), trainingTypes.get(0));
        assertEquals(trainings.get(1).getTrainingType(), trainingTypes.get(1));
        assertEquals(trainings.get(2).getTrainingType(), trainingTypes.get(0));
        assertEquals(trainings.get(3).getTrainingType(), trainingTypes.get(1));
        assertEquals(trainings.get(4).getTrainingType(), trainingTypes.get(0));
        assertEquals(trainings.get(5).getTrainingType(), trainingTypes.get(1));
    }
}
