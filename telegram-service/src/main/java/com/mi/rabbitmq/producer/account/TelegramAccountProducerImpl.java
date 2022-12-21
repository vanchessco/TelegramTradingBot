package com.mi.rabbitmq.producer.account;


import account.Account;
import com.mi.rabbitmq.config.RabbitMQProducerConfig;
import com.mi.rabbitmq.consumer.account.TelegramAccountConsumer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TelegramAccountProducerImpl implements TelegramAccountProducer {

    private TelegramAccountConsumer consumer;
    private RabbitTemplate rabbitTemplate;

    @Override
    @Cacheable(value = "accounts")
    public List<Account.TinkoffAccount> getAccounts() {
        String request = "request_accounts";
        rabbitTemplate.convertAndSend(RabbitMQProducerConfig.QUEUE_NAME_PRODUCER, request);
        log.info("Message send: {}", request);
        List<Account.TinkoffAccount> accounts = consumer.getAccounts();

        return accounts;
    }

    @Override
    public Account.TinkoffAccount getAccount(String accountID) {
        String request = "request_one_account" + "," + accountID;
        rabbitTemplate.convertAndSend(RabbitMQProducerConfig.QUEUE_NAME_PRODUCER, request);
        log.info("Message send: {}", request);
        Account.TinkoffAccount account = consumer.getAccount();

        return account;
    }

    @Override
    public List<Account.Position> getPositions(String accountID) {
        String request = "request_positions" + "," + accountID;
        rabbitTemplate.convertAndSend(RabbitMQProducerConfig.QUEUE_NAME_PRODUCER, request);
        log.info("Message send: {}", request);
        List<Account.Position> positionList = consumer.getPositions();

        return positionList;
    }

}
