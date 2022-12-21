package com.mi;

import com.mi.config.TelegramBot;
import com.mi.rabbitmq.config.RabbitMQProducerConfig;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * Hello world!
 */
@SpringBootApplication
@AllArgsConstructor
@EnableEurekaClient
@EnableCaching
public class TelegramApp {
    private TelegramBot telegramBot;
    private RabbitAdmin rabbitAdmin;

    public static void main(String[] args) {
        SpringApplication.run(TelegramApp.class);

    }


    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void destroy() {
        rabbitAdmin.purgeQueue(RabbitMQProducerConfig.QUEUE_NAME_CONSUMER);
        rabbitAdmin.purgeQueue(RabbitMQProducerConfig.QUEUE_NAME_PRODUCER);
    }
}
