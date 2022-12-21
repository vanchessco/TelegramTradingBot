package com.mi.rabbit_mq_consumer.rabbit_mq.consumer.message.sender;

import com.mi.rabbit_mq_consumer.rabbit_mq.config.RabbitMQConsumerConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MessageSenderImpl implements MessageSender {

    private RabbitTemplate rabbitTemplate;


    @Override
    public void convertAndSend(byte[] array, String correlationId) {
        Message response = MessageBuilder
                .withBody(array)
                .setCorrelationId(correlationId)
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConsumerConfig.QUEUE_NAME_CONSUMER, response);
        log.info("Message send: {}", response);
    }
}
