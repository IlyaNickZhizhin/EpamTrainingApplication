package org.epam.model;

import org.epam.Reader;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ModelTest {

    Reader reader = new Reader();
    Trainee trainee3;
    Trainee trainee4;
    User user1;

    {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        user1 = reader.readEntity("users/user1", User.class);
        trainee3 = reader.readEntity("trainees/trainee3", Trainee.class);
        trainee4 = reader.readEntity("trainees/trainee4", Trainee.class);
    }

    @Test
    public void testUserEqualsAndHashCode() {
        User user2 = user1;
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
        user2 = reader.readEntity("users/user2", User.class);
        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testTrainerEqualsAndHashCode() {
        Trainer trainer1 = reader.readEntity("trainers/trainer1", Trainer.class);
        Trainer trainer2 = reader.readEntity("trainers/trainer1", Trainer.class);
        assertEquals(trainer1, trainer2);
        assertEquals(trainer1.hashCode(), trainer2.hashCode());
        trainer2.setId(2);
        assertNotEquals(trainer1, trainer2);
        assertNotEquals(trainer1.hashCode(), trainer2.hashCode());
    }

    @Test
    public void testTraineeEqualsAndHashCode() {
        Trainee trainee1 = reader.readEntity("trainees/trainee3", Trainee.class);
        Trainee trainee2 = reader.readEntity("trainees/trainee3", Trainee.class);
        assertEquals(trainee1, trainee2);
        assertEquals(trainee1.hashCode(), trainee2.hashCode());
        trainee2 = trainee4;
        assertNotEquals(trainee1, trainee2);
        assertNotEquals(trainee1.hashCode(), trainee2.hashCode());
    }

    @Test
    public void testTrainingEqualsAndHashCode() {
        Training training1 = reader.readEntity("trainings/training1", Training.class);
        Training training2 = reader.readEntity("trainings/training1", Training.class);
        assertEquals(training1, training2);
        assertEquals(training1.hashCode(), training2.hashCode());
        training2 = reader.readEntity("trainings/training2", Training.class);
        assertNotEquals(training1, training2);
        assertNotEquals(training1.hashCode(), training2.hashCode());
    }


}
