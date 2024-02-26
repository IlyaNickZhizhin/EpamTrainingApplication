package org.epam.gymservice.integration.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@ActiveProfiles("test")
@RunWith(Cucumber.class)
@CucumberOptions(
        features = {
                "src/test/resources/features/training.feature"
                ,"src/test/resources/features/trainee.feature",
                "src/test/resources/features/trainer.feature",
                "src/test/resources/features/login.feature"},
        glue = "org.epam.gymservice.integration.stepdefenition",
        plugin = {"pretty", "html:target/cucumber-reports.html"}
)
@TestPropertySource(locations = "classpath:application.yaml")
public class ApiIntegrationTest {

    private static JdbcTemplate jdbcTemplate;
    public static final PostgreSQLContainer POSTGRE_SQL_CONTAINER_LOGIN
            = new PostgreSQLContainer("postgres")
            .withDatabaseName("test_epam_training_app")
            .withUsername("epamtrainee")
            .withPassword("epamtraineepassword");

    @BeforeClass
    public static void setUp() {
        POSTGRE_SQL_CONTAINER_LOGIN.start();
        System.setProperty(
                "spring.datasource.url",
                POSTGRE_SQL_CONTAINER_LOGIN.getJdbcUrl()
        );
    }

    @AfterClass
    public static void tearDown() {
        POSTGRE_SQL_CONTAINER_LOGIN.stop();
    }

}
