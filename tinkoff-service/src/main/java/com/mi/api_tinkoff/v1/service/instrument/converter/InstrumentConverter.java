package com.mi.api_tinkoff.v1.service.instrument.converter;

import com.mi.library.compiled_proto.schedule.ExchangeSchedule;
import instrument.Instrument;
import ru.tinkoff.piapi.contract.v1.TradingDay;

public interface InstrumentConverter {

    Instrument.TinkoffInstrument convert(String ticker);

    ExchangeSchedule.Schedule convert(TradingDay tradingDay);
}
