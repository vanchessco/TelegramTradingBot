package com.mi.api_tinkoff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableEurekaClient
@EnableCaching
@EnableFeignClients
public class ApiTinkoffApp {
    public static void main(String[] args) {
        SpringApplication.run(ApiTinkoffApp.class);
    }
}
