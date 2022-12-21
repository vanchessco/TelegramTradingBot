package com.mi.rabbit_mq_consumer.rabbit_mq.config;


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
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RabbitMQConsumerConfig {

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

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Connection connection() {
        return connectionFactory().createConnection();
    }

    @Bean
    public Channel channel() {
        return connection().createChannel(false);
    }


    @Bean
    public Queue queueProducer() {
        return new Queue(QUEUE_NAME_PRODUCER);
    }

    @Bean
    public Queue queueConsumer() {
        return new Queue(QUEUE_NAME_CONSUMER);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    @Bean
    public Binding bindingForProducer() {
        return BindingBuilder.bind(queueProducer()).to(directExchange()).with(ROUTING_KEY_PRODUCER);
    }

    @Bean
    public Binding bindingForConsumer() {
        return BindingBuilder.bind(queueConsumer()).to(directExchange()).with(ROUTING_KEY_CONSUMER);
    }


}
