package com.mi.api_tinkoff.v1.service.instrument.converter;


import com.mi.api_tinkoff.config.tinkoff_api_config.TinkoffContextProvider;
import com.mi.api_tinkoff.v1.service.converter.ConverterService;
import com.mi.library.compiled_proto.schedule.ExchangeSchedule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Instrument;
import ru.tinkoff.piapi.contract.v1.TradingDay;
import ru.tinkoff.piapi.core.exception.ApiRuntimeException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class InstrumentConverterImpl implements InstrumentConverter {

    private TinkoffContextProvider contextProvider;
    private ConverterService converterService;

    @Override
    public instrument.Instrument.TinkoffInstrument convert(String ticker) {
        Instrument i;
        instrument.Instrument.TinkoffInstrument tinkoffInstrument = null;
        try {
            i = contextProvider.getInstrumentsService().getInstrumentByTickerSync(ticker, "TQBR");
            var instrumentPrice = ConverterService.quotationToBigDecimal(converterService.instrumentLastPrice(i.getFigi()));

            tinkoffInstrument = instrument.Instrument.TinkoffInstrument.newBuilder()
                    .setUID(i.getUid())
                    .setFigi(i.getFigi())
                    .setTicker(i.getTicker())
                    .setName(i.getName())
                    .setType(i.getInstrumentType())
                    .setCountryOfRiskName(i.getCountryOfRiskName())
                    .setLot(i.getLot())
                    .setCurrency(i.getCurrency())
                    .setCurrentInstrumentPrice(instrument.Instrument.TinkoffInstrument.Money.newBuilder()
                            .setPrice(instrumentPrice.toString())
                            .setCurrency(i.getCurrency()))
                    .setCurrentLotPrice(instrument.Instrument.TinkoffInstrument.Money.newBuilder()
                            .setPrice(instrumentPrice.multiply(BigDecimal.valueOf(i.getLot())).setScale(2, RoundingMode.HALF_UP).toString())
                            .setCurrency(i.getCurrency()))
                    .build();

        } catch (ApiRuntimeException ignored) {
        }


        return tinkoffInstrument;
    }

    @Override
    public ExchangeSchedule.Schedule convert(TradingDay tradingDay) {
        ExchangeSchedule.Schedule schedule = ExchangeSchedule.Schedule.newBuilder()
                .setDate(ConverterService.timeStampToLocalDateTime(tradingDay.getDate()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .setDayOfWeek(ConverterService.timeStampToLocalDateTime(tradingDay.getDate()).getDayOfWeek().toString())
                .setIsTradingDay(tradingDay.getIsTradingDay())
                .setStartTime(ConverterService.timeStampToLocalDateTime(tradingDay.getStartTime()).format(DateTimeFormatter.ofPattern("hh:mm")))
                .setEndTime(ConverterService.timeStampToLocalDateTime(tradingDay.getEndTime()).format(DateTimeFormatter.ofPattern("HH:mm")))
                .build();

        return schedule;
    }
}
