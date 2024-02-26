package org.epam.gymservice.integration.util;

import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@TestPropertySource(locations = "classpath:application.yaml")
public class DataBaseContainers {

    public static final PostgreSQLContainer POSTGRE_SQL_CONTAINER
            = new PostgreSQLContainer("postgres")
            .withDatabaseName("test_epam_training_app")
            .withUsername("epamtrainee")
            .withPassword("epamtraineepassword");
}
