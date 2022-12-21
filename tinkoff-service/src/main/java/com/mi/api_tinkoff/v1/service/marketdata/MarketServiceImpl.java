package com.mi.api_tinkoff.v1.service.marketdata;

import com.mi.api_tinkoff.config.tinkoff_api_config.TinkoffContextProvider;
import com.mi.api_tinkoff.v1.service.converter.ConverterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
@AllArgsConstructor
public class MarketServiceImpl implements MarketService {


    private TinkoffContextProvider contextProvider;


    @Override
    public List<HistoricCandle> findHistoricCandles(String figi) {
        Instant from = LocalDate.now().atStartOfDay(ZoneId.of("Europe/Moscow")).minus(1, ChronoUnit.YEARS).toInstant();
        Instant to = LocalDate.now().atStartOfDay(ZoneId.of("Europe/Moscow")).toInstant();

        List<HistoricCandle> candles = contextProvider.getMarketDataService().getCandlesSync(
                        figi,
                        from,
                        to,
                        CandleInterval.CANDLE_INTERVAL_DAY)
                .stream()
                .filter(h -> LocalDate
                        .from(ConverterService.timeStampToLocalDateTime(h.getTime()))
                        .getDayOfWeek()
                        .equals(DayOfWeek.FRIDAY))
                .toList();

        return candles;
    }


}
