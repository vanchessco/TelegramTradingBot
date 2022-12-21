package com.mi.rabbitmq.consumer.instrument;


import com.mi.library.compiled_proto.schedule.ExchangeSchedule;
import com.mi.rabbitmq.config.RabbitMQProducerConfig;
import com.mi.rabbitmq.config.utils.ConsumerUtils;
import instrument.Instrument;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class TelegramInstrumentConsumer {

    private ConsumerUtils consumerUtils;


    public Map<String, Map<BigDecimal, Integer>> getEffectivePortfolio() {
        Message message = consumerUtils.checkQueue(RabbitMQProducerConfig.QUEUE_NAME_CONSUMER, "effective_portfolio_consumer");
        log.info("Message received");
        Map<String, Map<BigDecimal, Integer>> effectivePortfolio = (Map<String, Map<BigDecimal, Integer>>) consumerUtils.deserialize(message.getBody());

        return effectivePortfolio;
    }

    public List<String> getFirstLevelTickers() {
        Message message = consumerUtils.checkQueue(RabbitMQProducerConfig.QUEUE_NAME_CONSUMER, "first_level_shares_consumer");
        log.info("Message received");
        List<String> tickers = (List<String>) consumerUtils.deserialize(message.getBody());

        return tickers;
    }

    public Instrument.TinkoffInstrument getInstrument() {
        Message message = consumerUtils.checkQueue(RabbitMQProducerConfig.QUEUE_NAME_CONSUMER, "instrument_consumer");
        log.info("Message received");
        Instrument.TinkoffInstrument instrument = (Instrument.TinkoffInstrument) consumerUtils.deserialize(message.getBody());

        return instrument;

    }

    public List<ExchangeSchedule.Schedule> getSchedule() {
        Message message = consumerUtils.checkQueue(RabbitMQProducerConfig.QUEUE_NAME_CONSUMER, "trading_days_consumer");
        log.info("Message received");
        List<ExchangeSchedule.Schedule> schedule = (List<ExchangeSchedule.Schedule>) consumerUtils.deserialize(message.getBody());

        return schedule;
    }
}
