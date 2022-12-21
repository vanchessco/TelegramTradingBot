package com.mi.api_tinkoff.v1.service.instrument.utils;

import ru.tinkoff.piapi.contract.v1.TradingDay;

import java.util.List;

public interface InstrumentUtils {

    List<String> tickerFilter();

    List<TradingDay> removeNonTradingDays();
}
