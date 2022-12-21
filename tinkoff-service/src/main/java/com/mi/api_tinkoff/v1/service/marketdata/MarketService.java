package com.mi.api_tinkoff.v1.service.marketdata;


import ru.tinkoff.piapi.contract.v1.HistoricCandle;

import java.util.List;

public interface MarketService {
    List<HistoricCandle> findHistoricCandles(String figi);

}
