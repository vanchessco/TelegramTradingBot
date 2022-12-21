package com.mi.api_tinkoff.v1.service.instrument;


import ru.tinkoff.piapi.contract.v1.Instrument;

public interface InstrumentService {


    Instrument findInstrumentByFigi(String figi);

    byte[] findInstrumentByTicker(String ticker);

    byte[] findFirstLevelShares();

    byte[] findSchedule();

    byte[] findEffectivePortfolio();


}
