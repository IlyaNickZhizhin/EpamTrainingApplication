package org.epam.reportservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.text.SimpleDateFormat;
import java.util.Arrays;

@EnableTransactionManagement
@Slf4j
@Configuration
@Profile("!cloud")
public class JmsConfig {

    @Value("${activemq.broker-url}")
    private String brokerUrl;
    @Value("${activemq.user}")
    private String username;
    @Value("${activemq.password}")
    private String password;

    @Bean
    public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory(){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(getActiveMqConnectionFactory());
        factory.setConcurrency("1-1");
        factory.setTransactionManager(jmsTransactionManager());
        factory.setMessageConverter(jacksonConverter());
        factory.setErrorHandler(
                t -> log.error("Message was not received by {}, with cause {}",
                        t.getMessage(), t.getCause().getMessage()));
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

    @Bean
    public PlatformTransactionManager jmsTransactionManager(){
        return new JmsTransactionManager(getActiveMqConnectionFactory());
    }

    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate jmsTemplate = new JmsTemplate(getActiveMqConnectionFactory());
        jmsTemplate.setDeliveryPersistent(true);
        jmsTemplate.setSessionTransacted(true);
        jmsTemplate.setMessageConverter(jacksonConverter());
        return jmsTemplate;
    }

    @Bean
    public MessageConverter jacksonConverter(){
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        converter.setObjectMapper(mapper);
        return converter;
    }

}
