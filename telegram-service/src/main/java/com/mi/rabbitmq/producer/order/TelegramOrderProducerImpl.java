package com.mi.rabbitmq.producer.order;


import com.mi.rabbitmq.config.RabbitMQProducerConfig;
import com.mi.rabbitmq.consumer.order.TelegramOrderConsumer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class TelegramOrderProducerImpl implements TelegramOrderProducer {

    private RabbitTemplate rabbitTemplate;
    private TelegramOrderConsumer orderConsumer;

    public String getOrderResponse(String action, String data) {
        String request = "request_order_" + action + "," + data;
        rabbitTemplate.convertAndSend(RabbitMQProducerConfig.QUEUE_NAME_PRODUCER, request);
        log.info("Message send: {}", request);
        String response = orderConsumer.getOrderResponse();

        return response;
    }

}
