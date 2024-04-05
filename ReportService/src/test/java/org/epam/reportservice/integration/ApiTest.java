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

    public static final GenericContainer containerMongo =
            new GenericContainer("mongo").withExposedPorts(27017);
    public static final GenericContainer containerAMQ =
            new GenericContainer("apache/activemq-artemis:latest-alpine")
                    .withExposedPorts(61616, 8161)
                    .withEnv("ANONYMOUS_LOGIN", "true");

    @BeforeClass
    public static void setUp(){
        containerMongo.start();
        String ipMongo = containerMongo.getContainerIpAddress();
        String portMongo = containerMongo.getMappedPort(27017).toString();
        String dbConnect = "mongodb://" + ipMongo + ":" + portMongo + "/gymWorkloads";
        System.setProperty(
                "spring.data.mongodb.uri",
                dbConnect
        );
        containerAMQ.start();
        String ipAMQ = containerAMQ.getContainerIpAddress();
        String portAMQ = containerAMQ.getMappedPort(61616).toString();
        String brokerUrl = "tcp://"+ipAMQ+":" + portAMQ;
        System.setProperty(
                "activemq.broker-url",
                brokerUrl
        );
    }


    @AfterClass
    public static void destroy(){
        containerMongo.stop(); containerAMQ.stop();
    }
}
