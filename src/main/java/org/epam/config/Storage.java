package org.epam.config;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


import jakarta.annotation.PreDestroy;
import lombok.Getter;
import jakarta.annotation.PostConstruct;
import org.epam.exceptions.StorageException;
import org.epam.model.Trainer;
import org.epam.model.Trainee;
import org.epam.model.Training;
import org.epam.model.User;
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
@PropertySource("classpath:application.properties")
public class Storage {

    @Value("${initFile}")
    private String initFile;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, User> users = new HashMap<>();
    private Map<Integer, Trainer> trainers = new HashMap<>();
    private Map<Integer, Trainee> trainees = new HashMap<>();
    private Map<Integer, Training> trainings = new HashMap<>();

    Logger logger = LoggerFactory.getLogger(Storage.class);

    @PostConstruct
    public void init() {
        logger.info("Begin reading initial file");
        try {
            InputStream inputStream = new FileInputStream(initFile);
            Map<String, List<Map<String, Object>>> data = objectMapper.readValue(inputStream, new TypeReference<Map<String, List<Map<String, Object>>>>() {});
            List<Map<String, Object>> usersData = data.get("users");
            List<Map<String, Object>> trainersData = data.get("trainers");
            List<Map<String, Object>> traineesData = data.get("trainees");
            List<Map<String, Object>> trainingsData = data.get("trainings");
            for (Map<String, Object> userMap:
                 usersData) {
                User user = objectMapper.convertValue(userMap, User.class);
                users.put(user.getUsername(), user);
            }
            logger.info("Users data read");
            for (Map<String, Object> trainerMap:
                    trainersData) {
                Trainer trainer = objectMapper.convertValue(trainerMap, Trainer.class);
                trainers.put(Integer.valueOf(trainer.getId()), trainer);
            }
            logger.info("Trainers data read");
            for (Map<String, Object> traineeMap:
                    traineesData) {
                Trainee trainee = objectMapper.convertValue(traineeMap, Trainee.class);
                trainees.put(Integer.valueOf(trainee.getId()), trainee);
            }
            logger.info("Trainees data read");
            for (Map<String, Object> trainingMap:
                    trainingsData) {
                Training training = objectMapper.convertValue(trainingMap, Training.class);
                trainings.put(Integer.valueOf(training.getId()), training);
            }
            logger.info("Trainings data read");

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
        data.put("users", new ArrayList<>(users.values()));
        logger.info("Users data saved");
        data.put("trainers", new ArrayList<>(trainers.values()));
        logger.info("Trainers data saved");
        data.put("trainees", new ArrayList<>(trainees.values()));
        logger.info("Trainees data saved");
        data.put("trainings", new ArrayList<>(trainings.values()));
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
                ",\n trainers=\n" + trainers +
                ",\n trainees=\n" + trainees +
                ",\n trainings=\n" + trainings +
                '}';
    }
}
