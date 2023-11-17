package org.epam.config;

import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("org.epam")
@PropertySource("classpath:application.yaml")
public class Config {

    @Bean
    public ModelMapper getMapper() {
        return new ModelMapper();
    }

}
