package com.mi.api_moex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Hello world!
 */

@SpringBootApplication
@EnableEurekaClient
public class ApiMoexApp {
    public static void main(String[] args) {
        SpringApplication.run(ApiMoexApp.class);
    }
}
