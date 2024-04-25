package org.epam.gymservice.config.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
@Profile("cloud")
public class JmsConfigSQS {

    @Bean
    public SqsClient sqsConnectionFactory(){
        return SqsClient.builder()
                .region(Region.EU_NORTH_1)
                .build();
    }

}
