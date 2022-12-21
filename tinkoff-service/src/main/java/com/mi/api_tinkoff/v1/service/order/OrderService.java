package com.mi.api_tinkoff.v1.service.order;


import com.mi.library.old.order.OrderRequest;

public interface OrderService {

    byte[] postOrder(OrderRequest request);

}
