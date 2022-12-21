package com.mi.rabbitmq.producer.instrument;


import com.mi.library.compiled_proto.schedule.ExchangeSchedule;
import instrument.Instrument;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TelegramInstrumentProducer {

    Instrument.TinkoffInstrument getInstrument(String ticker);

    Map<String, Map<BigDecimal, Integer>> getEffectivePortfolio();

    List<String> getFirstLevelTickers();

    List<ExchangeSchedule.Schedule> getSchedule();

    void resetEffectivePortfolio();

}
