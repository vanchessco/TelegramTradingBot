package com.mi.api_tinkoff.config.tinkoff_api_config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource("classpath:/field.properties")
public class TinkoffApiConfig {


    @Value(value = "${token}")
    private String token;


    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public TinkoffApiParameters parameters() {
        return new TinkoffApiParameters(token, false);
    }

    @Bean
    public TinkoffApiConnector apiConnector() {
        return new TinkoffApiConnector(parameters());
    }

    @Bean
    public TinkoffContextProvider contextProvider() {
        return new TinkoffContextProvider(apiConnector().getInvestApi());
    }


}
