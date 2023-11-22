package org.epam.testBeans.storageInFile;

import lombok.Getter;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Getter
public class Storage {

    public static HashMap<Integer, User> users = new HashMap<>();
    public static HashMap<Integer, Trainee> trainees = new HashMap<>();
    public static HashMap<Integer, Trainer> trainers = new HashMap<>();
    public static HashMap<Integer, Training> trainings = new HashMap<>();
    public static HashMap<Integer, TrainingType> trainingTypes = new HashMap<>();
}
