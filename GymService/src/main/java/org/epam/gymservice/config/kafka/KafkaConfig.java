package org.epam.gymservice.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.epam.common.dto.TrainerWorkloadRequest;
import org.epam.gymservice.dto.trainingDto.AddTrainingRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private List<String> bootstrapServers;


    @Bean
    public ProducerFactory<String, AddTrainingRequest> producerFactoryAdd() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public ProducerFactory<String, TrainerWorkloadRequest> producerFactoryDelete() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, AddTrainingRequest> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactoryAdd());
    }

    @Bean KafkaTemplate<String, TrainerWorkloadRequest> kafkaAddDLQTemplate() {
        return new KafkaTemplate<>(producerFactoryDelete());
    }

    @Bean
    public NewTopic addWorkloadTopic(){
        return new NewTopic("add-workload-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic deleteWorkloadTopic(){
        return new NewTopic("delete-workload-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic addWorkloadTopicDLQ(){
        return new NewTopic("add-workload-DLQ-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic deleteWorkloadTopicDLQ(){
        return new NewTopic("delete-workload-DLQ-topic", 3, (short) 1);
    }

}
