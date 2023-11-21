package org.epam.testBeans.storageInFile;

import jakarta.annotation.PreDestroy;
import org.epam.exceptions.StorageException;
import org.epam.model.gymModel.Trainer;
import org.epam.testBeans.filereader.FileToModelsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataSaver {

    FileToModelsMapper mapper;

    public DataSaver(FileToModelsMapper mapper) {
        this.mapper = mapper;
    }

    Logger logger = LoggerFactory.getLogger(DataSaver.class);

    @PreDestroy
    public void saveData() {
        Map<String, List<Object>> data = new HashMap<>();
        data.put("users", new ArrayList<>(mapper.getStorage().getUsers().values()));
        logger.info("Users data saved");
        data.put(mapper.takeDataName(Trainer.class.getName()),
                new ArrayList<>(mapper.getStorage().getModels(Trainer.class.getName()).values()));
        logger.info("Trainers data saved");
//        data.put(mapper.takeDataName(Trainee.class.getName()),
//                new ArrayList<>(mapper.getStorage().getModels(Trainee.class.getName()).values()));
//        logger.info("Trainees data saved");
//        data.put(mapper.takeDataName(Training.class.getName()),
//                new ArrayList<>(mapper.getStorage().getModels(Training.class.getName()).values()));
//        logger.info("Trainings data saved");

        try {
            logger.info("Begin saving data");
            mapper.writeModels();
            logger.info("Data saved");
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении данных", e);
        } catch (StorageException e) {
            throw new StorageException("Ошибка при сохранении данных");
        }
    }

}
