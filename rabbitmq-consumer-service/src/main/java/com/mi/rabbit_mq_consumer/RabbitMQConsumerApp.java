package com.mi.rabbit_mq_consumer;

import com.mi.rabbit_mq_consumer.rabbit_mq.config.RabbitMQConsumerConfig;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableRabbit
@AllArgsConstructor
public class RabbitMQConsumerApp {
    private RabbitAdmin rabbitAdmin;

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        SpringApplication.run(RabbitMQConsumerApp.class);

    }

    @PreDestroy
    public void destroy() {
        rabbitAdmin.purgeQueue(RabbitMQConsumerConfig.QUEUE_NAME_CONSUMER);
        rabbitAdmin.purgeQueue(RabbitMQConsumerConfig.QUEUE_NAME_PRODUCER);
    }
}
