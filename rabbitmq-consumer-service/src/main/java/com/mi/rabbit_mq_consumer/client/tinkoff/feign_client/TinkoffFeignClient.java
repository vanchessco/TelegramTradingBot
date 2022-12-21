package com.mi.rabbit_mq_consumer.client.tinkoff.feign_client;


import com.mi.library.old.order.OrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "tinkoff")
public interface TinkoffFeignClient {


    //AccountRestController
    @GetMapping(path = "/api/v1/account/all")
    ResponseEntity<byte[]> getAccounts();

    @GetMapping(path = "/api/v1/account/portfolio/position_list")
    ResponseEntity<byte[]> getPositions(@RequestParam("id") String accountID);

    @GetMapping(path = "/api/v1/account")
    ResponseEntity<byte[]> getAccount(@RequestParam("id") String accountID);


    //InstrumentRestController
    @GetMapping(path = "/api/v1/instrument")
    ResponseEntity<byte[]> getInstrumentByTicker(@RequestParam("ticker") String ticker);

    @GetMapping(path = "/api/v1/instrument/trading_days")
    ResponseEntity<byte[]> getSchedule();

    @GetMapping(path = "/api/v1/instrument/portfolio/effective")
    ResponseEntity<byte[]> getEffectivePortfolio();

    @GetMapping(path = "/api/v1/instrument/shares/first_level")
    ResponseEntity<byte[]> getFirstLevelShares();


    //OrderRestController
    @PostMapping(path = "/api/v1/order/submit_order")
    ResponseEntity<byte[]> postOrder(@RequestBody OrderRequest request);


    //OperationRestController
    @GetMapping(path = "/api/v1/operation/report/operations")
    ResponseEntity<byte[]> getOperations(@RequestParam("id") String accountID);


}
