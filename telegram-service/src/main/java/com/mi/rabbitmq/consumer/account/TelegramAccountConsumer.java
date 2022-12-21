package com.mi.rabbitmq.consumer.account;


import account.Account;
import com.mi.rabbitmq.config.RabbitMQProducerConfig;
import com.mi.rabbitmq.config.utils.ConsumerUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TelegramAccountConsumer {

    private ConsumerUtils consumerUtils;


    public List<Account.TinkoffAccount> getAccounts() {
        Message message = consumerUtils.checkQueue(RabbitMQProducerConfig.QUEUE_NAME_CONSUMER, "accounts_consumer");
        log.info("Message received");
        List<Account.TinkoffAccount> accounts = (List<Account.TinkoffAccount>) consumerUtils.deserialize(message.getBody());

        return accounts;
    }


    public account.Account.TinkoffAccount getAccount() {
        Message message = consumerUtils.checkQueue(RabbitMQProducerConfig.QUEUE_NAME_CONSUMER, "account_consumer");
        log.info("Message received");
        Account.TinkoffAccount account = (Account.TinkoffAccount) consumerUtils.deserialize(message.getBody());
        return account;
    }


    public List<Account.Position> getPositions() {
        Message message = consumerUtils.checkQueue(RabbitMQProducerConfig.QUEUE_NAME_CONSUMER, "positions_consumer");
        log.info("Message received");
        List<Account.Position> positions = (List<Account.Position>) consumerUtils.deserialize(message.getBody());

        return positions;
    }


}
