package org.epam.testBeans.filereader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.epam.model.User;
import org.epam.model.gymModel.Model;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.epam.testBeans.storageInFile.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
@Getter
@Setter
@PropertySource("classpath:application.properties")
public class FileToModelsMapper<M extends Model> {

    @Value("${initFile}")
    private String initFile;
    private Storage storage;

    @Autowired
    public FileToModelsMapper(Storage storage) {
        this.storage = storage;
    }

    public FileToModelsMapper(Storage storage, String customPathToInitFile) {
        this.storage = storage;
        initFile = customPathToInitFile;
    }

    Logger logger = LoggerFactory.getLogger(FileToModelsMapper.class);
    private ObjectMapper objectMapper;
    Map<String, List<Map<String, Object>>> loadData;
    Map<String, List<Object>> saveData;

    @PostConstruct
    public void init() throws IOException {
        objectMapper = new ObjectMapper();
        InputStream inputStream = new FileInputStream(initFile);
        loadData = objectMapper.readValue(inputStream, new TypeReference<>() {
        });
    }

    public void initUsers() {
        List<Map<String, Object>> usersData = loadData.get("users");
        for (Map<String, Object> userMap :
                usersData) {
            User user = objectMapper.convertValue(userMap, User.class);
            storage.getUsers().put(user.getUsername(), user);
            logger.info("users data read");
        }
    }

    public void initTrainingTypes() {
        List<Map<String, Object>> trainingTypeData = loadData.get("training_types");
        for (Map<String, Object> trainingTypeMap :
                trainingTypeData) {
            TrainingType trainingType = objectMapper.convertValue(trainingTypeMap, TrainingType.class);
            storage.getModels(TrainingType.class.getName()).put(trainingType.getId(), trainingType);
            logger.info("TrainingType data read");
        }
    }

    public void initTrainers() {
        List<Map<String, Object>> trainerData = loadData.get("trainers");
        for (Map<String, Object> trainerTypeMap :
                trainerData) {
            Integer id = (Integer) trainerTypeMap.get("id");
            Integer userId = (Integer) trainerTypeMap.get("userId");
            Integer specId = (Integer) trainerTypeMap.get("specialization");
            Trainer trainer = new Trainer();
            trainer.setUser(getUser(userId));
            trainer.setSpecialization((TrainingType) storage.getModels(TrainingType.class.getName()).get(specId));
            storage.getModels(Trainer.class.getName()).put(trainer.getId(), trainer);
            logger.info("Trainers data read");
        }
    }

    public void initTrainees() {
        List<Map<String, Object>> traineeData = loadData.get("trainees");
        for (Map<String, Object> traineeTypeMap :
                traineeData) {
            Integer id = (Integer) traineeTypeMap.get("id");
            Integer userId = (Integer) traineeTypeMap.get("userId");
            String dateOfBirthStr = (String) traineeTypeMap.get("dateOfBirth");
            LocalDate dateOfBirth = dateOfBirthStr!=null ? LocalDate.parse(dateOfBirthStr) : null;
            String address = (String) traineeTypeMap.get("address");
            Trainee trainee = new Trainee();
            trainee.setUser(getUser(userId));
            trainee.setDateOfBirth(dateOfBirth);
            trainee.setAddress(address);
            storage.getModels(Trainee.class.getName()).put(trainee.getId(), trainee);
            logger.info("Trainee data read");
        }
    }

    public void initTrainings() {
        List<Map<String, Object>> trainingData = loadData.get("trainings");
        for (Map<String, Object> trainingTypeMap :
                trainingData) {
            Integer id = (Integer) trainingTypeMap.get("id");
            Integer trainerId = (Integer) trainingTypeMap.get("trainerId");
            Integer traineeId = (Integer) trainingTypeMap.get("traineeId");
            Integer trainingTypeId = (Integer) trainingTypeMap.get("trainingTypeId");
            Double duration = (Double) trainingTypeMap.get("duration");
            String dateStr = (String) trainingTypeMap.get("trainingDate");
            LocalDate date = LocalDate.parse(dateStr);
            Training training = new Training();
            training.setTrainer((Trainer) storage.getModels(Trainer.class.getName()).get(trainerId));
            training.setTrainee((Trainee) storage.getModels(Trainee.class.getName()).get(traineeId));
            training.setTrainingType((TrainingType) storage.getModels(TrainingType.class.getName()).get(trainingTypeId));
            training.setTrainingDate(date);
            training.setDuration(duration);
            storage.getModels(Training.class.getName()).put(training.getId(), training);
            logger.info("Training data read");
        }
    }

    public void writeModels() throws IOException {
        String jsonData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(loadData);
        Files.write(Paths.get(initFile), jsonData.getBytes());
    }


    public String takeDataName(String modelClassGetName) {
        StringBuilder sb = new StringBuilder(modelClassGetName);
        return sb.substring(sb.lastIndexOf(".") + 1).toLowerCase().concat("s");
    }

    private User getUser(Integer userId) {
        User user = null;
        List<User> users = new ArrayList<>(storage.getUsers().values());
        Iterator<User> userIterator = users.iterator();
        while (user == null) {
            User u = userIterator.next();
            if (u.getId() == userId) {
                user = u;
            }
        }
        return user;
    }
}
