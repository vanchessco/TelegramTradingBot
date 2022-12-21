package com.mi.api_tinkoff.v1.service.instrument;


import com.mi.api_tinkoff.config.tinkoff_api_config.TinkoffContextProvider;
import com.mi.api_tinkoff.v1.service.converter.ConverterService;
import com.mi.api_tinkoff.v1.service.instrument.converter.InstrumentConverter;
import com.mi.api_tinkoff.v1.service.instrument.utils.InstrumentUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Instrument;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class InstrumentServiceImpl implements InstrumentService {

    private TinkoffContextProvider contextProvider;
    private InstrumentConverter instrumentConverter;
    private InstrumentUtils instrumentUtils;


    @Override
    public Instrument findInstrumentByFigi(String figi) {
        return contextProvider.getInstrumentsService().getInstrumentByFigiSync(figi);
    }

    @Override
    public byte[] findInstrumentByTicker(String ticker) {
        return ConverterService.serialize(instrumentConverter.convert(ticker));
    }


    @Override
    public byte[] findFirstLevelShares() {
        return ConverterService.serialize(instrumentUtils.tickerFilter());
    }

    @Override
    public byte[] findSchedule() {
        var tradingDays = instrumentUtils.removeNonTradingDays();
        var schedule = tradingDays.stream()
                .map(tradingDay -> instrumentConverter.convert(tradingDay))
                .collect(Collectors.toList());
        return ConverterService.serialize(schedule);
    }


    @Override
    public byte[] findEffectivePortfolio() {
        Map<String, Map<BigDecimal, Integer>> map = new HashMap();
        Map<BigDecimal, Integer> map2 = new HashMap<>();
        map2.put(BigDecimal.ZERO, Integer.MIN_VALUE);
        map.put("Вырезанные рассчеты", map2);

        return ConverterService.serialize(map);
    }


}
