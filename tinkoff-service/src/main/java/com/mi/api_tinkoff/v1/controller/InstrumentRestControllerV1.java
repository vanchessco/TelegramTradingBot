package com.mi.api_tinkoff.v1.controller;

import com.mi.api_tinkoff.v1.service.instrument.InstrumentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/instrument")
@AllArgsConstructor
public class InstrumentRestControllerV1 {

    private InstrumentService instrumentService;


    @GetMapping
    public ResponseEntity<byte[]> getInstrumentByTicker(@RequestParam String ticker) {
        var instrument = instrumentService.findInstrumentByTicker(ticker);
        return new ResponseEntity<>(instrument, HttpStatus.OK);
    }


    @GetMapping(path = "/shares/first_level")
    public ResponseEntity<byte[]> getFirstLevelShares() {
        var positions = instrumentService.findFirstLevelShares();
        return new ResponseEntity<>(positions, HttpStatus.OK);
    }


    @GetMapping(path = "/trading_days")
    public ResponseEntity<byte[]> getSchedule() {
        var schedule = instrumentService.findSchedule();
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }


    @GetMapping(path = "/portfolio/effective")
    public ResponseEntity<byte[]> getEffectivePortfolio() {
        var effectivePortfolio = instrumentService.findEffectivePortfolio();
        return new ResponseEntity<>(effectivePortfolio, HttpStatus.OK);
    }


}
