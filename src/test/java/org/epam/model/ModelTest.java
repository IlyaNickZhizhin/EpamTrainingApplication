package org.epam.model;

import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.epam.TestDatabaseInitializer.trainee3;
import static org.epam.TestDatabaseInitializer.trainee4;
import static org.epam.TestDatabaseInitializer.user1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ModelTest {

    @Test
    public void testUserEqualsAndHashCode() {
        User user1 = TestDatabaseInitializer.user1;
        User user2 = user1;
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
        user2 = TestDatabaseInitializer.user2;
        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testTrainerEqualsAndHashCode() {
        Trainer trainer1 = new Trainer();
        trainer1.setId(1);
        trainer1.setSpecialization(new TrainingType());
        trainer1.setUser(new User(1, "T", "U", "TU", "p", true));

        Trainer trainer2 = new Trainer();
        trainer2.setId(1);
        trainer2.setSpecialization(new TrainingType());
        trainer2.setUser(new User(1, "T", "U", "TU", "p", true));

        assertEquals(trainer1, trainer2);
        assertEquals(trainer1.hashCode(), trainer2.hashCode());

        trainer2.setId(2);
        assertNotEquals(trainer1, trainer2);
        assertNotEquals(trainer1.hashCode(), trainer2.hashCode());
    }

    @Test
    public void testTraineeEqualsAndHashCode() {
        Trainee trainee1 = new Trainee();
        trainee1.setId(1);
        trainee1.setAddress("a");
        trainee1.setDateOfBirth(LocalDate.of(2000,1,1));
        trainee1.setUser(user1);
        Trainee trainee2 = trainee3;
        trainee2.setId(1);
        trainee2.setAddress("a");
        trainee2.setDateOfBirth(LocalDate.of(2000,1,1));
        trainee2.setUser(user1);
        assertEquals(trainee1, trainee2);
        assertEquals(trainee1.hashCode(), trainee2.hashCode());
        trainee2 = trainee4;
        assertNotEquals(trainee1, trainee2);
        assertNotEquals(trainee1.hashCode(), trainee2.hashCode());
    }

    @Test
    public void testTrainingEqualsAndHashCode() {
        Training training1 = TestDatabaseInitializer.training1;
        Training training2 = TestDatabaseInitializer.training1;
        assertEquals(training1, training2);
        assertEquals(training1.hashCode(), training2.hashCode());
        training2 = TestDatabaseInitializer.training2;
        assertNotEquals(training1, training2);
        assertNotEquals(training1.hashCode(), training2.hashCode());
    }


}
