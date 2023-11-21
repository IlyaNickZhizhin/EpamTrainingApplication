package org.epam.config;

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
