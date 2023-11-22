package org.epam.testBeans.filereader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.epam.model.User;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
@Getter
@Setter
@PropertySource("classpath:application.properties")
public class FileToModelsWriter {

    @Value("${initFile}")
    private String initFile;
    private Storage storage;

    @Autowired
    public FileToModelsWriter(Storage storage) {
        this.storage = storage;
    }

    public FileToModelsWriter(Storage storage, String customPathToInitFile) {
        this.storage = storage;
        initFile = customPathToInitFile;
    }

    Logger logger = LoggerFactory.getLogger(FileToModelsWriter.class);
    private ObjectMapper objectMapper;
    Map<String, List<Map<String, Object>>> loadData;
    Map<String, List<Object>> saveData;

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
            storage.users.put(user.getId(), user);
            logger.info("users data read");
        }
    }

    public void initTrainingTypes() {
        List<Map<String, Object>> trainingTypeData = loadData.get("training_types");
        for (Map<String, Object> trainingTypeMap :
                trainingTypeData) {
            TrainingType trainingType = objectMapper.convertValue(trainingTypeMap, TrainingType.class);
            storage.trainingTypes.put(trainingType.getId(), trainingType);
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
            trainer.setSpecialization(storage.trainingTypes.get(specId));
            Storage.trainers.put(trainer.getId(), trainer);
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
            Storage.trainees.put(trainee.getId(), trainee);
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
            training.setTrainer(storage.trainers.get(trainerId));
            training.setTrainee(storage.trainees.get(traineeId));
            training.setTrainingType((TrainingType) storage.trainingTypes.get(trainingTypeId));
            training.setTrainingDate(date);
            training.setDuration(duration);
            Storage.trainings.put(training.getId(), training);
            logger.info("Training data read");
        }
    }

    private User getUser(Integer userId) {
        User user = null;
        List<User> users = new ArrayList<>(storage.users.values());
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
