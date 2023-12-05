package org.epam.config;

import lombok.RequiredArgsConstructor;
import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan("org.epam")
@PropertySource("classpath:application.properties")
@Import({
        SpringDocConfiguration.class,
        SpringDocWebMvcConfiguration.class,
        SwaggerConfig.class,
        SwaggerUiConfigProperties.class,
        SwaggerUiOAuthProperties.class,
        JacksonAutoConfiguration.class
})
public class Config implements WebMvcConfigurer {
}