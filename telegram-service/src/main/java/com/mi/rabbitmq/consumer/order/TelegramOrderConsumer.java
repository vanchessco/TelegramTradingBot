package com.mi.rabbitmq.consumer.order;

import com.mi.rabbitmq.config.RabbitMQProducerConfig;
import com.mi.rabbitmq.config.utils.ConsumerUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class TelegramOrderConsumer {


    private ConsumerUtils consumerUtils;

    public String getOrderResponse() {
        Message message = consumerUtils.checkQueue(RabbitMQProducerConfig.QUEUE_NAME_CONSUMER, "post_order_response_consumer");
        log.info("Message received:{}", message);
        String response = (String) consumerUtils.deserialize(message.getBody());

        return response;
    }
}
