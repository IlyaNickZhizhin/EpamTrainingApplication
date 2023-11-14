package org.epam.config;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


import jakarta.annotation.PreDestroy;
import lombok.Getter;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.epam.exceptions.StorageException;
import org.epam.model.*;
import org.epam.model.gymModel.Model;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Component
@Getter
@Setter
@PropertySource("classpath:application.properties")
public class Storage<T extends Model> {

    @Value("${initFile}")
    private String initFile;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, Map<Integer, T>> gymModels;
    private Map<String, User> users;

    public Storage() {
        this.gymModels = new HashMap<>();
        this.users = new HashMap<>();
        gymModels.put("trainers", new HashMap<Integer, T>());
        gymModels.put("trainees", new HashMap<Integer, T>());
        gymModels.put("trainings", new HashMap<Integer, T>());
    }

    public Map<Integer, Trainer> getTrainers() {
        return (Map<Integer, Trainer>) gymModels.get("trainers");
    }

    public Map<Integer, Trainee> getTrainees() {
        return (Map<Integer, Trainee>) gymModels.get("trainees");
    }

    public Map<Integer, Training> getTrainings() {
        return (Map<Integer, Training>) gymModels.get("trainings");
    }

    Logger logger = LoggerFactory.getLogger(Storage.class);

    @PostConstruct
    public void init() {
        logger.info("Begin reading initial file");
        try {
            InputStream inputStream = new FileInputStream(initFile);
            Map<String, List<Map<String, Object>>> data = objectMapper.readValue(inputStream, new TypeReference<>() {});
            initUsers(data);
            initTrainers(data);
            initTrainees(data);
            initTrainings(data);
        } catch (StreamReadException e) {
            throw new RuntimeException("Error in reading file", e);
        } catch (DatabindException e) {
            throw new RuntimeException("Error in mapping file", e);
        } catch (IOException e) {
            throw new RuntimeException("Error in parsing file", e);
        }
    }
    @PreDestroy
    public void saveData() {
        Map<String, List<Object>> data = new HashMap<>();
        data.put("users", new ArrayList<>(getUsers().values()));
        logger.info("Users data saved");
        data.put("trainers", new ArrayList<>(getTrainers().values()));
        logger.info("Trainers data saved");
        data.put("trainees", new ArrayList<>(getTrainees().values()));
        logger.info("Trainees data saved");
        data.put("trainings", new ArrayList<>(getTrainings().values()));
        logger.info("Trainings data saved");

        try {
            logger.info("Begin saving data");
            String jsonData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            Files.write(Paths.get(initFile), jsonData.getBytes());
            logger.info("Data saved");
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении данных", e);
        } catch (StorageException e) {
            throw new StorageException("Ошибка при сохранении данных");
        }
    }

    @Override
    public String toString() {
        return "Storage{\n" +
                "users=\n" + users +
                ",\n trainers=\n" + gymModels.get("trainers") +
                ",\n trainees=\n" + gymModels.get("trainee") +
                ",\n trainings=\n" + gymModels.get("trainings") +
                '}';
    }

    private void initUsers(Map<String, List<Map<String, Object>>> data){
        List<Map<String, Object>> usersData = data.get("users");
        for (Map<String, Object> userMap:
                usersData) {
            User user = objectMapper.convertValue(userMap, User.class);
            users.put(user.getUsername(), user);
            logger.info("Users data read");
        }
    }

    private void initTrainers(Map<String, List<Map<String, Object>>> data){
        List<Map<String, Object>> trainersData = data.get("trainers");
        for (Map<String, Object> trainerMap:
                trainersData) {
            Trainer trainer = objectMapper.convertValue(trainerMap, Trainer.class);
            getTrainers().put(Integer.valueOf(trainer.getId()), trainer);
            logger.info("Trainers data read");
        }
    }

    private void initTrainees(Map<String, List<Map<String, Object>>> data){
        List<Map<String, Object>> traineesData = data.get("trainees");
        for (Map<String, Object> traineeMap:
                traineesData) {
            Trainee trainee = objectMapper.convertValue(traineeMap, Trainee.class);
            getTrainees().put(trainee.getId(), trainee);
            logger.info("Trainees data read");
        }
    }

    private void initTrainings(Map<String, List<Map<String, Object>>> data){
        List<Map<String, Object>> trainingsData = data.get("trainings");
        for (Map<String, Object> trainingMap:
                trainingsData) {
            Training training = objectMapper.convertValue(trainingMap, Training.class);
            getTrainings().put(training.getId(), training);
            logger.info("Trainings data read");
        }
    }

}
