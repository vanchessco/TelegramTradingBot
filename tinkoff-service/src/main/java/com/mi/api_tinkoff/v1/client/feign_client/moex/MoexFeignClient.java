package com.mi.api_tinkoff.v1.client.feign_client.moex;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "moex", path = "/api/v1/collections")
public interface MoexFeignClient {

    @GetMapping(path = "/first_level_shares")
    ResponseEntity<List<String>> getFirstLevelShares();
}
