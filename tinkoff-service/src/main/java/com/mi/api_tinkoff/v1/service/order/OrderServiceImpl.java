package com.mi.api_tinkoff.v1.service.order;

import com.mi.api_tinkoff.config.tinkoff_api_config.TinkoffContextProvider;
import com.mi.api_tinkoff.v1.service.converter.ConverterService;
import com.mi.api_tinkoff.v1.service.instrument.InstrumentService;
import com.mi.library.old.order.OrderRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final TinkoffContextProvider contextProvider;
    private final InstrumentService instrumentService;
    private final ConverterService converterService;


    @Override
    public byte[] postOrder(OrderRequest request) {
        return "Вырезанные вычисления".getBytes();
    }
}
