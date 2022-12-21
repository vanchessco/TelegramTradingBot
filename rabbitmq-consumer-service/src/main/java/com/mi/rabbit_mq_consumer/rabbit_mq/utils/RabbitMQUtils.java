package com.mi.rabbit_mq_consumer.rabbit_mq.utils;

import com.mi.library.old.order.OrderRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class RabbitMQUtils {
    public OrderRequest createOrderRequest(String[] data, long lots, int direction, int type) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAccount_ID(data[1]);
        orderRequest.setFigi(data[2]);
        orderRequest.setLots(lots);
        orderRequest.setDirection(direction);
        orderRequest.setType(type);

        return orderRequest;
    }

}
