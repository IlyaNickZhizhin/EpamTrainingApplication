package org.epam.gymservice.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;

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
    public ActiveMQConnectionFactory getActiveMqConnectionFactory(){
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setUserName(username);
        factory.setPassword(password);
        factory.setBrokerURL(brokerUrl);
        RedeliveryPolicy policy = factory.getRedeliveryPolicy();
        policy.setMaximumRedeliveries(2);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(){
        return new JmsTemplate(getActiveMqConnectionFactory());
    }
}
