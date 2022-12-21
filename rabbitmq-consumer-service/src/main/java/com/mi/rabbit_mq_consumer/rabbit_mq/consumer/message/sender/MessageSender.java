package com.mi.rabbit_mq_consumer.rabbit_mq.consumer.message.sender;

public interface MessageSender {
    void convertAndSend(byte[] array, String correlationId);
}
