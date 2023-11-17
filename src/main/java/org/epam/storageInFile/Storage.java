package org.epam.storageInFile;

import jakarta.annotation.PreDestroy;
import lombok.Getter;
import jakarta.annotation.PostConstruct;
import org.epam.model.*;
import org.epam.model.gymModel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class Storage<M extends Model> {

/*     TODO - не стоит смешивать код с логикой и код с данными (сервисы и дто) -
        вот тут явно сломан single responsibility принцип, класс и делает логику по сохранению данных,
        и их же хранит - лучше инит методы вынести в отдельный класс, который будет делать логику
        по сохранению в некий "persistence" уровень (твой класс Storage с данными)
        *******************************ВЫПОЛНИЛ КАК СМОГ***********************************

        TODO - и зачем там наследование от некоего базового дто и метод toString?
         toString - убрал
         <M extends Model> - пока оставил, использу для объявления Map<String, Map<Integer, M>> в строке 37,
           планирую обсудить на созвоне.

*/


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
        gymModels.put(TrainingType.class.getName(), new HashMap<>());
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
