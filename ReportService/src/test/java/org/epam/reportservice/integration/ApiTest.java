package org.epam.reportservice.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Slf4j
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/workload.feature",
        glue = "org.epam.reportservice.integration",
        plugin = {"pretty", "html:target/cucumber-reports.html"})
public class ApiTest {

    public static final GenericContainer containerMongo =
            new GenericContainer("mongo").withExposedPorts(27017);
    public static final Network network = Network.newNetwork();
    public static final GenericContainer containerZookeeper =
            new GenericContainer("confluentinc/cp-zookeeper:latest")
                    .withExposedPorts(2222)
                    .withEnv("ZOOKEEPER_CLIENT_PORT", "2222")
                    .withNetworkAliases("zookeeper")
                    .withNetwork(network)
                    .waitingFor(Wait.forLogMessage(".*binding to port.*\\n", 1));
    public static GenericContainer containerKafka=
            new GenericContainer("confluentinc/cp-kafka:latest")
                    .withExposedPorts(9999)
                    .withNetwork(network)
                    .withEnv("KAFKA_ZOOKEEPER_CONNECT", "zookeeper:2222")
                    .withEnv("KAFKA_ADVERTISED_LISTENERS", "PLAINTEXT://localhost:9999")
                    .waitingFor(Wait.forLogMessage(".*Kafka version.*\\n", 1));
    @BeforeClass
    public static void setUp() throws InterruptedException {
        containerMongo.start();
        String ipMongo = containerMongo.getContainerIpAddress();
        String portMongo = containerMongo.getMappedPort(27017).toString();
        String dbConnect = "mongodb://" + ipMongo + ":" + portMongo + "/gymWorkloads";
        System.setProperty(
                "spring.data.mongodb.uri",
                dbConnect
        );
        containerZookeeper.start();
        containerKafka.setPortBindings(Arrays.asList("9999:9999"));
        containerKafka.start();
        String ipKafka = containerKafka.getContainerIpAddress();
        String portKafka = containerKafka.getMappedPort(9999).toString();
        String brokerUrl = "PLAINTEXT://" + ipKafka + ":" + portKafka;
        System.setProperty(
                "spring.kafka.bootstrap-servers",
                brokerUrl
        );
        log.info(brokerUrl);
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, brokerUrl);
        try (AdminClient adminClient = AdminClient.create(props)) {
            List<NewTopic> topics = new ArrayList<>();
            topics.add(new NewTopic("add-workload-DLQ-topic", 3, (short) 1));
            topics.add(new NewTopic("delete-workload-DLQ-topic", 3, (short) 1));
            adminClient.createTopics(topics);
        }
    }

    @AfterClass
    public static void destroy(){
        containerMongo.stop(); containerZookeeper.stop(); containerKafka.stop();
    }
}
