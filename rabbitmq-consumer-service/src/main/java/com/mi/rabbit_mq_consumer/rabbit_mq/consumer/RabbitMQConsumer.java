package com.mi.rabbit_mq_consumer.rabbit_mq.consumer;


import com.mi.library.old.order.OrderRequest;
import com.mi.rabbit_mq_consumer.client.tinkoff.feign_client.TinkoffFeignClient;
import com.mi.rabbit_mq_consumer.rabbit_mq.config.RabbitMQConsumerConfig;
import com.mi.rabbit_mq_consumer.rabbit_mq.consumer.message.converter.MessageConverter;
import com.mi.rabbit_mq_consumer.rabbit_mq.consumer.message.sender.MessageSender;
import com.mi.rabbit_mq_consumer.rabbit_mq.utils.RabbitMQUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
@RabbitListener(queues = RabbitMQConsumerConfig.QUEUE_NAME_PRODUCER)
public class RabbitMQConsumer {

    private TinkoffFeignClient tinkoffFeignClient;
    private RabbitMQUtils rabbitMQUtils;
    private MessageSender messageSender;

    @RabbitHandler
    public void handleMessage(String message) {
        String[] data = message.split(",");
        message = MessageConverter.updateMessage(message);
        switch (message) {
            case "request_accounts" -> {
                byte[] accounts = tinkoffFeignClient.getAccounts().getBody();
                messageSender.convertAndSend(accounts, "accounts_consumer");
            }
            case "request_one_account" -> {
                String accountID = data[1];
                byte[] account = tinkoffFeignClient.getAccount(accountID).getBody();
                messageSender.convertAndSend(account, "account_consumer");
            }
            case "request_instrument" -> {
                String ticker = data[1];
                byte[] instrument = tinkoffFeignClient.getInstrumentByTicker(ticker).getBody();
                messageSender.convertAndSend(instrument, "instrument_consumer");
            }
            case "request_operations" -> {
                String accountID = data[1];
                byte[] operationResponse = tinkoffFeignClient.getOperations(accountID).getBody();
                messageSender.convertAndSend(operationResponse, "operations_consumer");
            }
            case "request_positions" -> {
                String accountID = data[1];
                byte[] positions = tinkoffFeignClient.getPositions(accountID).getBody();
                messageSender.convertAndSend(positions, "positions_consumer");
            }
            case "request_schedule" -> {
                byte[] schedule = tinkoffFeignClient.getSchedule().getBody();
                messageSender.convertAndSend(schedule, "trading_days_consumer");
            }
            case "request_effective_portfolio" -> {
                byte[] effectivePortfolio = tinkoffFeignClient.getEffectivePortfolio().getBody();
                messageSender.convertAndSend(effectivePortfolio, "effective_portfolio_consumer");
            }
            case "request_first_level_shares" -> {
                byte[] shares = tinkoffFeignClient.getFirstLevelShares().getBody();
                messageSender.convertAndSend(shares, "first_level_shares_consumer");
            }
            case "buy" -> {
                OrderRequest orderRequest = rabbitMQUtils.createOrderRequest(data, 1L, 1, 2);
                byte[] orderResponse = tinkoffFeignClient.postOrder(orderRequest).getBody();
                messageSender.convertAndSend(orderResponse, "post_order_response_consumer");
            }
            case "sell" -> {
                OrderRequest orderRequest = rabbitMQUtils.createOrderRequest(data, 1L, 2, 2);
                byte[] orderResponse = tinkoffFeignClient.postOrder(orderRequest).getBody();
                messageSender.convertAndSend(orderResponse, "post_order_response_consumer");
            }
        }

    }


}
