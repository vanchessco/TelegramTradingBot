package com.mi.api_tinkoff.v1.service.instrument.utils;


import com.mi.api_tinkoff.config.tinkoff_api_config.TinkoffContextProvider;
import com.mi.api_tinkoff.v1.client.feign_client.moex.MoexFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.contract.v1.TradingDay;
import ru.tinkoff.piapi.contract.v1.TradingSchedule;
import ru.tinkoff.piapi.core.exception.ApiRuntimeException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InstrumentUtilsService implements InstrumentUtils {

    private MoexFeignClient moexFeignClient;
    private TinkoffContextProvider contextProvider;


    @Override
    public List<String> tickerFilter() {
        List<String> tickerList = moexFeignClient.getFirstLevelShares().getBody();
        List<String> filtered = new ArrayList<>();
        tickerList.forEach(ticker -> {
            try {
                var instrument = contextProvider.getInstrumentsService().getShareByTickerSync(ticker, "TQBR");
                filtered.add(ticker);
            } catch (ApiRuntimeException ignored) {
            }
        });

        return filtered;
    }

    @Override
    public List<TradingDay> removeNonTradingDays() {
        Instant from = Instant.now();
        Instant to = null;

        LocalDate dateFrom = LocalDate.ofInstant(from, ZoneId.of("Europe/Moscow"));
        switch (dateFrom.getDayOfWeek()) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> to = from.plus(5, ChronoUnit.DAYS);
            case SATURDAY -> {
                from = Instant.now().plus(2, ChronoUnit.DAYS);
                to = from.plus(4, ChronoUnit.DAYS);
            }
            case SUNDAY -> {
                from = Instant.now().plus(1, ChronoUnit.DAYS);
                to = from.plus(5, ChronoUnit.DAYS);
            }
        }
        TradingSchedule schedule = contextProvider.getInstrumentsService().getTradingScheduleSync("moex", from, to);
        List<TradingDay> tradingDays = schedule.getDaysList().stream().filter(TradingDay::getIsTradingDay).collect(Collectors.toList());

        return tradingDays;
    }
}
