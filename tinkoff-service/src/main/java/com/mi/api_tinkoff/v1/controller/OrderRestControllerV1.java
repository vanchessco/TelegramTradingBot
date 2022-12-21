package com.mi.api_tinkoff.v1.controller;


import com.mi.api_tinkoff.v1.service.order.OrderService;
import com.mi.library.old.order.OrderRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/order")
@AllArgsConstructor
public class OrderRestControllerV1 {


    private OrderService orderService;


    @PostMapping(path = "/submit_order")
    public ResponseEntity<byte[]> postOrder(@RequestBody OrderRequest request) {
        byte[] response = orderService.postOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
