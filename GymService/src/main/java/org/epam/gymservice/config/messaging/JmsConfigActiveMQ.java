package org.epam.gymservice.config.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
@Profile("!cloud")
@RequiredArgsConstructor
public class JmsConfigActiveMQ {
    @Value("${activemq.broker-url:defaultURL}")
    private String brokerUrl;
    @Value("${activemq.user:defaultUSERNAME}")
    private String username;
    @Value("${activemq.password:defaultPassword}")
    private String password;
    private final ObjectMapper mapper;

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
        JmsTemplate template = new JmsTemplate(getActiveMqConnectionFactory());
        template.setMessageConverter(jacksonConverter());
        return template;
    }

    @Bean
    public MessageConverter jacksonConverter(){
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        converter.setObjectMapper(mapper);
        return converter;
    }
}
