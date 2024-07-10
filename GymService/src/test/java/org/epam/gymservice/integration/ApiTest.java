package org.epam.gymservice.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Slf4j
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
public class ApiTest {

    private static JdbcTemplate jdbcTemplate;
    private static final Network network = Network.newNetwork();
    public static final PostgreSQLContainer POSTGRE_SQL_CONTAINER_LOGIN
            = new PostgreSQLContainer("postgres")
            .withDatabaseName("test_epam_training_app")
            .withUsername("epamtrainee")
            .withPassword("epamtraineepassword");
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
    public static void setUp() {
        POSTGRE_SQL_CONTAINER_LOGIN.start();
        System.setProperty(
                "spring.datasource.url",
                POSTGRE_SQL_CONTAINER_LOGIN.getJdbcUrl()
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
            topics.add(new NewTopic("add-workload-topic", 3, (short) 1));
            topics.add(new NewTopic("delete-workload-topic", 3, (short) 1));
            adminClient.createTopics(topics);
        }
    }

    @AfterClass
    public static void tearDown() {
        POSTGRE_SQL_CONTAINER_LOGIN.stop(); containerZookeeper.stop(); containerKafka.stop();
    }

}
