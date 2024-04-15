package org.epam.gymservice.config.without;

import jakarta.annotation.PostConstruct;
import org.h2.tools.Server;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;

import java.sql.SQLException;

@Configuration
@Profile("without")
public class WithoutDbConfig {

    @PostConstruct
    public void startH2Server() {
        try {
            Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to start H2 server: ", e);
        }
    }

    @Bean
    public JmsTemplate jmsFakeTemplate() {
        return Mockito.mock(JmsTemplate.class);
    }

}
