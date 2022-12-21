package com.mi.api_tinkoff.config.tinkoff_api_config;

import ru.tinkoff.piapi.core.*;


public class TinkoffContextProvider {

    private final InvestApi investApi;

    public TinkoffContextProvider(InvestApi investApi) {
        this.investApi = investApi;
    }


    public InstrumentsService getInstrumentsService() {
        return investApi.getInstrumentsService();

    }

    public UsersService getUsersService() {
        return investApi.getUserService();
    }

    public OrdersService getOrdersService() {
        return investApi.getOrdersService();
    }

    public OperationsService getOperationsService() {
        return investApi.getOperationsService();
    }

    public MarketDataService getMarketDataService() {
        return investApi.getMarketDataService();
    }


}
