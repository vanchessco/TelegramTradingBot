package com.mi.rabbitmq.config;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQProducerConfig {

    public static final String QUEUE_NAME_PRODUCER = "queue_producer";
    public static final String QUEUE_NAME_CONSUMER = "queue_consumer";


    public static final String DIRECT_EXCHANGE = "exchange.direct";
    public static final String ROUTING_KEY_PRODUCER = "routing_key_send";
    public static final String ROUTING_KEY_CONSUMER = "routing_key_receive";


    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("localhost");
    }

    @Bean
    public RabbitAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public Connection connection() {
        Connection connection = connectionFactory().createConnection();
        return connection;
    }

    @Bean
    public Channel channel() {
        Channel channel = connection().createChannel(false);
        return channel;
    }


    @Bean
    public Queue queueForProducer() {
        return new Queue(QUEUE_NAME_PRODUCER);
    }

    @Bean
    public Queue queueForConsumer() {
        return new Queue(QUEUE_NAME_CONSUMER);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    @Bean
    public Binding bindingForQueueProducer() {
        return BindingBuilder.bind(queueForProducer()).to(directExchange()).with(ROUTING_KEY_PRODUCER);
    }

    @Bean
    public Binding bindingForQueueConsumer() {
        return BindingBuilder.bind(queueForConsumer()).to(directExchange()).with(ROUTING_KEY_CONSUMER);
    }


}
