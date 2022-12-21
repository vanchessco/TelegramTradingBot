package com.mi.api_tinkoff.config.tinkoff_api_config;


import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.piapi.core.InvestApi;


@Slf4j
public class TinkoffApiConnector {

    private final TinkoffApiParameters apiParameters;
    private InvestApi investApi;


    public TinkoffApiConnector(TinkoffApiParameters apiParameters) {
        this.apiParameters = apiParameters;
    }

    public InvestApi getInvestApi() {
        if (investApi == null) {
            log.info("Connecting to Tinkoff API...");
            if (apiParameters.isSandBoxMode()) {
                investApi = InvestApi.createSandbox(apiParameters.getToken());
                System.out.println("Created sandbox connection");
            } else {
                investApi = InvestApi.create(apiParameters.getToken());
                System.out.println("Created full access connection");
            }
            log.info("Connection created.");
        }
        return investApi;
    }
}
