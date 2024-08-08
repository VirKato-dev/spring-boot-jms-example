package ru.otus.example.jms.config;

import com.rabbitmq.jms.admin.RMQConnectionFactory;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class RabbitMqConfig {

    public static final String JMS_TEMPLATE_RABBIT = "rabbitMqJmsTemplate";
    public static final String JMS_TEMPLATE_ACTIVE = "activeMqJmsTemplate";
    public static final String JMS_LISTENER_CONTAINER_FACTORY_RABBIT = "rabbitMqJmsListenerContainerFactory";
    public static final String JMS_LISTENER_CONTAINER_FACTORY_ACTIVE = "activeMqJmsListenerContainerFactory";

    private static final String CONNECTION_FACTORY_RABBIT = "rabbitMqConnectionFactory";
    private static final String CONNECTION_FACTORY_ACTIVE = "activeMqConnectionFactory";

    public static final String EXCHANGE_NAME = "foo";
    public static final String QUEUE_NAME = "foo";
    public static final String CLASS_NAME = "className";

    @Bean(CONNECTION_FACTORY_RABBIT)
    public ConnectionFactory rabbitConnectionFactory(
            @Value("${jms.rabbit-host}") String host,
            @Value("${jms.rabbit-broker}") Integer port,
            @Value("${jms.user}") String user,
            @Value("${jms.password}") String password
    ) {
        RMQConnectionFactory connectionFactory = new RMQConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(user);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean(CONNECTION_FACTORY_ACTIVE)
    public ConnectionFactory activeConnectionFactory(
            @Value("${jms.active-host}") String host,
            @Value("${jms.active-broker}") Integer port,
            @Value("${jms.user}") String user,
            @Value("${jms.password}") String password
    ) {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(host + ":" + port);
        connectionFactory.setUserName(user);
        connectionFactory.setPassword(password);
        connectionFactory.setTrustAllPackages(true);
        return connectionFactory;
    }

    @Bean(JMS_TEMPLATE_RABBIT)
    public JmsTemplate jmsRabbitTemplate(@Qualifier(CONNECTION_FACTORY_RABBIT) ConnectionFactory cachingConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
        jmsTemplate.setReceiveTimeout(TimeUnit.SECONDS.toMillis(10));
        return jmsTemplate;
    }

    @Bean(JMS_LISTENER_CONTAINER_FACTORY_RABBIT)
    public JmsListenerContainerFactory<?> jmsRabbitListenerContainerFactory(
            @Qualifier(CONNECTION_FACTORY_RABBIT) ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean(JMS_TEMPLATE_ACTIVE)
    public JmsTemplate jmsActiveTemplate(@Qualifier(CONNECTION_FACTORY_ACTIVE) ConnectionFactory cachingConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
        jmsTemplate.setReceiveTimeout(TimeUnit.SECONDS.toMillis(10));
        return jmsTemplate;
    }

    @Bean(JMS_LISTENER_CONTAINER_FACTORY_ACTIVE)
    public JmsListenerContainerFactory<?> jmsActiveListenerContainerFactory(
            @Qualifier(CONNECTION_FACTORY_ACTIVE) ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

}
