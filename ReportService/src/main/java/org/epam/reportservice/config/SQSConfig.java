package org.epam.reportservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
@Profile("cloud")
public class SQSConfig {

    @Value("${aws.sqs.accessKeyId}")
    private String accessKey;

    @Value("${aws.sqs.secretKey}")
    private String secretKey;

    @Bean
    public SqsClient sqsCloudy(){
        return SqsClient.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(StaticCredentialsProvider
                        .create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

}
