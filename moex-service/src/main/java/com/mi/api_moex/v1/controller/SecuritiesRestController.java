package com.mi.api_moex.v1.controller;


import com.mi.api_moex.v1.service.securities.collections.SecuritiesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/collections")
@AllArgsConstructor
public class SecuritiesRestController {
    private SecuritiesService securitiesService;


    @GetMapping(path = "/first_level_shares")
    public ResponseEntity<List<String>> getFirstLevelShares() {
        List<String> shares = securitiesService.findFirstLevelShares();
        return new ResponseEntity<>(shares, HttpStatus.OK);
    }


}
