package org.epam.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.epam.model.User;
import org.epam.model.gymModel.Model;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.storageInFile.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
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
        loadData = objectMapper.readValue(inputStream, new TypeReference<>() {});
    }

    public void initUsers(){
        List<Map<String, Object>> usersData = loadData.get("users");
        for (Map<String, Object> userMap:
                usersData) {
            User user = objectMapper.convertValue(userMap, User.class);
            storage.getUsers().put(user.getUsername(), user);
            logger.info("users data read");
        }
    }

    public <T extends Model> void initModels(Class<T> modelClass){
        List<Map<String, Object>> modelsData = loadData.get(takeDataName(modelClass.getName()));
        for (Map<String, Object> modelMap: modelsData) {
            T model = objectMapper.convertValue(modelMap, modelClass);
            storage.getModels(modelClass.getName()).put(model.getId(), (M) model);
            logger.info(modelClass.getName() + " data read");
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
}
