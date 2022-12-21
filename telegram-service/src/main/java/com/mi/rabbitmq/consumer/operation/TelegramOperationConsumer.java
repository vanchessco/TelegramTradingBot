package com.mi.rabbitmq.consumer.operation;


import com.mi.rabbitmq.config.RabbitMQProducerConfig;
import com.mi.rabbitmq.config.utils.ConsumerUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class TelegramOperationConsumer {

    private ConsumerUtils consumerUtils;


    public String getOperations() {
        Message message = consumerUtils.checkQueue(RabbitMQProducerConfig.QUEUE_NAME_CONSUMER, "operations_consumer");
        log.info("Message received");
        String response = new String(message.getBody());

        return response;
    }
}
