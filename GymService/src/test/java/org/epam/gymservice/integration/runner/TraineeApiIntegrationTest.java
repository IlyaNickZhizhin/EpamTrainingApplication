package org.epam.gymservice.integration.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.epam.gymservice.integration.util.DataBaseContainers.POSTGRE_SQL_CONTAINER;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/trainee.feature",
        glue = "org.epam.gymservice.integration.stepdefenition",
        plugin = {"pretty", "html:target/cucumber-reports.html"}
)
public class TraineeApiIntegrationTest {

    private static JdbcTemplate jdbcTemplate;

    @BeforeClass
    public static void setUp() {
        POSTGRE_SQL_CONTAINER.start();
        System.setProperty(
                "spring.datasource.url",
                POSTGRE_SQL_CONTAINER.getJdbcUrl()
        );
    }

    @AfterClass
    public static void tearDown() {
        POSTGRE_SQL_CONTAINER.stop();
    }

}
