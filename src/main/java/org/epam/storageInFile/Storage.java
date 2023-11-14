package org.epam.storageInFile;

import jakarta.annotation.PreDestroy;
import lombok.Getter;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.epam.model.*;
import org.epam.model.gymModel.Model;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
@Setter
@PropertySource("classpath:application.properties")
public class Storage<M extends Model> {

    private DataSaver dataSaver;
    private Map<String, Map<Integer, M>> gymModels;
    private Map<String, User> users;

    public Map<Integer, M> getModels(String namespace) {
        return gymModels.get(namespace);
    }

    Logger logger = LoggerFactory.getLogger(Storage.class);

    @PostConstruct
    public void init() {
        gymModels = new HashMap<>();
        users = new HashMap<>();
        gymModels.put(Trainer.class.getName(), new HashMap<>());
        gymModels.put(Trainee.class.getName(), new HashMap<>());
        gymModels.put(Training.class.getName(), new HashMap<>());
        logger.info("Begin reading initial file");
    }

    @PreDestroy
    public void saveData() {
        dataSaver.saveData();
    }

}
