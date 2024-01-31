package org.epam.reportservice.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import java.util.Arrays;

@Configuration
@PropertySource("classpath:secretStore.yaml")
public class JmsConfig {

    @Value("${AMQ.url}")
    private String brokerUrl;
    @Value("${AMQ.login}")
    private String username;
    @Value("${AMQ.password}")
    private String password;

    @Bean
    public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory(){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(getActiveMqConnectionFactory());
        factory.setConcurrency("1-1");
        return factory;
    }

    @Bean
    public ActiveMQConnectionFactory getActiveMqConnectionFactory(){
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setUserName(username);
        factory.setPassword(password);
        factory.setBrokerURL(brokerUrl);
        factory.setTrustedPackages(Arrays.asList("org.epam.common.dto", "java.time"));
        return factory;
    }



}
