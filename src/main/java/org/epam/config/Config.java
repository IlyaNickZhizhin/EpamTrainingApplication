package org.epam.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"org.epam"})
public class Config {

    @Bean
    public ModelMapper getMapper() {
        return new ModelMapper();
    }

}
