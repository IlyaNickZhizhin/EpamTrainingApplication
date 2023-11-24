package org.epam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;

public class Reader {

    static String startPath ="";
    static String endPath ="";

    public void setStartPath(String startPath) {
        Reader.startPath = startPath;
    }

    public void setEndPath(String endPath) {
        Reader.endPath = endPath;
    }


    public<M> M readEntity(String path, Class<M> clazz) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = new FileInputStream(startPath +path+ endPath);
            return objectMapper.readValue(inputStream, clazz);
        } catch (IOException e) {
            return null;
        }
    }

    public User readUser(String path){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = new FileInputStream(startPath +path+ endPath);
            return objectMapper.readValue(inputStream, User.class);
        } catch (IOException e) {
            return null;
        }
    }

    public TrainingType readType(String path) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = new FileInputStream(startPath +path+ endPath);
            return objectMapper.readValue(inputStream, TrainingType.class);
        } catch (IOException e) {
            return null;
        }
    }

    public Trainer readTrainer(String path) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = new FileInputStream(startPath +path+ endPath);
            Map<String, Object> trainerMap = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            Trainer trainer = new Trainer();
            trainer.setId((Integer) trainerMap.get("id"));
            Integer specializationNumber = (Integer) trainerMap.get("specialization");
            Integer userNumber = (Integer) trainerMap.get("userId");
            trainer.setSpecialization(readType("trainingtypes/trainingType" + specializationNumber));
            trainer.setUser(readUser("users/user" + userNumber));
            return trainer;
        } catch (IOException e) {
            return null;
        }
    }

    public Trainee readTrainee(String path)  {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = new FileInputStream(startPath +path+ endPath);
            Map<String, Object> traineeMap = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            Trainee trainee = new Trainee();
            trainee.setId((Integer) traineeMap.get("id"));
            Integer userNumber = (Integer) traineeMap.get("userId");
            trainee.setUser(readUser("users/user" + userNumber));
            String dateOfBirthStr = (String) traineeMap.get("dateOfBirth");
            LocalDate dateOfBirth = dateOfBirthStr!=null ? LocalDate.parse(dateOfBirthStr) : null;
            trainee.setDateOfBirth(dateOfBirth);
            trainee.setAddress((String) traineeMap.get("address"));
            return trainee;
        } catch (IOException e) {
            return null;
        }
    }

    public Training readTraining(String path) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = new FileInputStream(startPath +path+ endPath);
            Map<String, Object> trainingMap = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            Training training = new Training();
            training.setId((Integer) trainingMap.get("id"));
            Integer traineeNumber = (Integer) trainingMap.get("traineeId");
            training.setTrainee(readTrainee("trainees/trainee" + traineeNumber));
            Integer trainerNumber = (Integer) trainingMap.get("trainerId");
            training.setTrainer(readTrainer("trainers/trainer" + trainerNumber));
            training.setTrainingName((String) trainingMap.get("trainingName"));
            Integer typeNumber = (Integer) trainingMap.get("trainingTypeId");
            training.setTrainingType(readType("trainingtypes/trainingType" + typeNumber));
            training.setTrainingDate(LocalDate.parse((String) trainingMap.get("trainingDate")));
            training.setDuration((Double) trainingMap.get("duration"));
        return training;
        } catch (IOException e) {
        return null;
        }
    }
}
