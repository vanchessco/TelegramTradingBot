package com.mi.rabbitmq.producer.order;

public interface TelegramOrderProducer {

    String getOrderResponse(String action, String data);
}
