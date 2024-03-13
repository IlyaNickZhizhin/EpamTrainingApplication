package org.epam.reportservice.integration;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.testcontainers.containers.GenericContainer;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/workload.feature",
        glue = "org.epam.reportservice.integration",
        plugin = {"pretty", "html:target/cucumber-reports.html"})
public class ApiTest {
    public static final GenericContainer container =
            new GenericContainer("mongo").withExposedPorts(27017);

    @BeforeClass
    public static void setUp(){
        container.start();
        String ip = container.getContainerIpAddress();
        String port = container.getMappedPort(27017).toString();
        String dbConnect = "mongodb://" + ip + ":" + port + "/gymWorkloads";
        System.setProperty(
                "spring.data.mongodb.uri",
                dbConnect
        );
    }


    @AfterClass
    public static void destroy(){
        container.stop();
    }
}
