package com.mi.rabbitmq.config.utils;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

@Service
@AllArgsConstructor
@Slf4j
public class ConsumerUtils {

    private RabbitTemplate rabbitTemplate;

    public Message checkQueue(String queueName, String correlationID) {
        Message message = null;
        boolean flag = true;
        while (flag) {
            message = rabbitTemplate.receive(queueName);
            if (message != null) {
                String corID = message.getMessageProperties().getCorrelationId();
                if (correlationID.equalsIgnoreCase(corID)) {
                    flag = false;
                }
            }
        }
        return message;
    }

    public Object deserialize(byte[] array) {
        try (ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(array);
             ObjectInputStream objectInputStream = new ObjectInputStream(arrayInputStream)) {
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
