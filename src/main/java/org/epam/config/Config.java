package org.epam.config;

import org.mapstruct.MapperConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("org.epam")
@PropertySource("classpath:application.properties")
public class Config {
}
