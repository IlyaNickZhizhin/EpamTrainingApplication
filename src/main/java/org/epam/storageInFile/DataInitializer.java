package org.epam.storageInFile;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.epam.mapper.FileToModelsMapper;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@DependsOn("fileToModelsMapper")
@RequiredArgsConstructor
public class DataInitializer {

    private final FileToModelsMapper mapper;
    Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @PostConstruct
    public void init() {
        logger.info("Begin reading initial file");
            mapper.initUsers();
            mapper.initModels(Trainer.class);
            mapper.initModels(Trainee.class);
            mapper.initModels(Training.class);
    }

}
