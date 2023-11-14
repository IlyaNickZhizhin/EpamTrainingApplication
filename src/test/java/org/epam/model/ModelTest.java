package org.epam.model;

import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ModelTest {

    @Test
    public void testUserEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(1);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setUsername("johndoe");
        user1.setPassword("password");
        user1.setActive(true);

        User user2 = new User();
        user2.setId(1);
        user2.setFirstName("John");
        user2.setLastName("Doe");
        user2.setUsername("johndoe");
        user2.setPassword("password");
        user2.setActive(true);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());

        user2.setId(2);
        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testTrainerEqualsAndHashCode() {
        Trainer trainer1 = new Trainer();
        trainer1.setId(1);
        trainer1.setSpecialization("Fitness");
        trainer1.setUserId(1);

        Trainer trainer2 = new Trainer();
        trainer2.setId(1);
        trainer2.setSpecialization("Fitness");
        trainer2.setUserId(1);

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
        trainee1.setDateOfBirth(null);
        trainee1.setAddress(null);
        trainee1.setUserId(1);
        Trainee trainee2 = new Trainee();
        trainee2.setId(1);
        trainee2.setDateOfBirth(null);
        trainee2.setAddress(null);
        trainee2.setUserId(1);
        assertEquals(trainee1, trainee2);
        assertEquals(trainee1.hashCode(), trainee2.hashCode());
        trainee2.setId(2);
        assertNotEquals(trainee1, trainee2);
        assertNotEquals(trainee1.hashCode(), trainee2.hashCode());
    }

    @Test
    public void testTrainingEqualsAndHashCode() {
        Training training1 = new Training();
        training1.setId(1);
        training1.setTraineeId(1);
        training1.setTrainerId(1);
        training1.setTrainingTypeId(1);
        training1.setTrainingDate(null);
        Training training2 = new Training();
        training2.setId(1);
        training2.setTraineeId(1);
        training2.setTrainerId(1);
        training2.setTrainingTypeId(1);
        training2.setTrainingDate(null);
        assertEquals(training1, training2);
        assertEquals(training1.hashCode(), training2.hashCode());
        training2.setId(2);
        assertNotEquals(training1, training2);
        assertNotEquals(training1.hashCode(), training2.hashCode());
    }


}
