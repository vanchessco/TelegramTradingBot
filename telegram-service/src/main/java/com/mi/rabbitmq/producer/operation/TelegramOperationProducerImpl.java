package com.mi.rabbitmq.producer.operation;


import com.mi.rabbitmq.config.RabbitMQProducerConfig;
import com.mi.rabbitmq.consumer.operation.TelegramOperationConsumer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class TelegramOperationProducerImpl implements TelegramOperationProducer {

    private RabbitTemplate rabbitTemplate;
    private TelegramOperationConsumer operationConsumer;

    @Override
    public String getOperations(String accountID) {
        String request = "request_operations" + "," + accountID;
        rabbitTemplate.convertAndSend(RabbitMQProducerConfig.QUEUE_NAME_PRODUCER, request);
        log.info("Message send: {}", request);
        String response = operationConsumer.getOperations();

        return response;
    }
}
