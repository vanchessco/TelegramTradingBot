package com.mi.api_tinkoff.v1.service.converter;


import com.google.protobuf.Timestamp;
import com.mi.api_tinkoff.config.tinkoff_api_config.TinkoffContextProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.MoneyValue;
import ru.tinkoff.piapi.contract.v1.Quotation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;


@Service
@AllArgsConstructor
public class ConverterService {


    private TinkoffContextProvider contextProvider;

    public static LocalDateTime timeStampToLocalDateTime(Timestamp timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(
                        timestamp.getSeconds(),
                        timestamp.getNanos()),
                ZoneId.of("Europe/Moscow")
        );
    }

    public static BigDecimal quotationToBigDecimal(Quotation value) {
        if (value == null) {
            return null;
        }
        return convert(value.getUnits(), value.getNano());
    }


    public static BigDecimal moneyValueToBigDecimal(MoneyValue value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }

        return convert(value.getUnits(), value.getNano());
    }

    private static BigDecimal convert(long units, int nano) {
        if (units == 0 && nano == 0) {
            return BigDecimal.ZERO;
        }
        String price = "." + nano;
        BigDecimal result = (BigDecimal.valueOf(units).add(BigDecimal.valueOf(Double.parseDouble(price)))).setScale(2, RoundingMode.HALF_UP);
        return result;
    }

    public static <T> byte[] serialize(T type) {
        try (ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
             ObjectOutputStream objectOutput = new ObjectOutputStream(byteArrayOutput)) {
            objectOutput.writeObject(type);
            return byteArrayOutput.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Quotation instrumentLastPrice(String figi) {
        var lastPrice = contextProvider.getMarketDataService().getLastPricesSync(List.of(figi)).get(0).getPrice();
        return Quotation.newBuilder().setUnits(lastPrice.getUnits()).setNano(lastPrice.getNano()).build();
    }

}
