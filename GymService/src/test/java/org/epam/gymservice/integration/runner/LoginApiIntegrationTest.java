package org.epam.gymservice.integration.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.epam.gymservice.integration.util.DataBaseContainers.POSTGRE_SQL_CONTAINER;

@ActiveProfiles("test")
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/login.feature",
        glue = "org.epam.gymservice.integration.stepdefenition",
        plugin = {"pretty", "html:target/cucumber-reports.html"}
)
@TestPropertySource(locations = "classpath:application.yaml")
public class LoginApiIntegrationTest {

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
