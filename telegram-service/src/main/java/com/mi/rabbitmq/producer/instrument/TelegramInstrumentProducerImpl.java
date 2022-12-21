package com.mi.rabbitmq.producer.instrument;

import com.mi.library.compiled_proto.schedule.ExchangeSchedule;
import com.mi.rabbitmq.config.RabbitMQProducerConfig;
import com.mi.rabbitmq.consumer.instrument.TelegramInstrumentConsumer;
import instrument.Instrument;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service
@AllArgsConstructor
@Slf4j
public class TelegramInstrumentProducerImpl implements TelegramInstrumentProducer {
    private TelegramInstrumentConsumer consumer;
    private RabbitTemplate rabbitTemplate;


    @Override
    public Instrument.TinkoffInstrument getInstrument(String ticker) {
        String request = "request_instrument" + "," + ticker;
        rabbitTemplate.convertAndSend(RabbitMQProducerConfig.QUEUE_NAME_PRODUCER, request);
        log.info("Message send: {}", request);
        Instrument.TinkoffInstrument instrument = consumer.getInstrument();

        return instrument;
    }

    @Override
    @Cacheable(value = "effective_portfolio")
    public Map<String, Map<BigDecimal, Integer>> getEffectivePortfolio() {
        String request = "request_effective_portfolio";
        rabbitTemplate.convertAndSend(RabbitMQProducerConfig.QUEUE_NAME_PRODUCER, request);
        log.info("Message send: {}", request);
        Map<String, Map<BigDecimal, Integer>> effectivePortfolio = consumer.getEffectivePortfolio();

        return effectivePortfolio;
    }

    @Override
    @Cacheable(value = "first_level_shares")
    public List<String> getFirstLevelTickers() {
        String request = "request_first_level_shares";
        rabbitTemplate.convertAndSend(RabbitMQProducerConfig.QUEUE_NAME_PRODUCER, request);
        log.info("Message send: {}", request);
        List<String> tickers = consumer.getFirstLevelTickers();

        return tickers;
    }

    @Override
    public List<ExchangeSchedule.Schedule> getSchedule() {
        String request = "request_schedule";
        rabbitTemplate.convertAndSend(RabbitMQProducerConfig.QUEUE_NAME_PRODUCER, request);
        List<ExchangeSchedule.Schedule> schedule = consumer.getSchedule();

        return schedule;
    }


    @Override
    @CacheEvict(value = "effective_portfolio", allEntries = true)
    public void resetEffectivePortfolio() {
    }

}
