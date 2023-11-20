package org.epam.config;

import org.epam.dao.GymAbstractDaoImpl;
import org.epam.dao.TraineeDaoImpl;
import org.epam.dao.TrainerDaoImpl;
import org.epam.dao.TrainingDaoImpl;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("org.epam")
@PropertySource("classpath:application.properties")
public class Config {
    @Bean
    public ModelMapper getMapper() {
        return new ModelMapper();
    }
}
