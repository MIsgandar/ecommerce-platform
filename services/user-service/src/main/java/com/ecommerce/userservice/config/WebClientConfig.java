package com.ecommerce.userservice.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(
            @Value("${product-service.url}") String productServiceUrl) {


        return WebClient.builder()
                .baseUrl(productServiceUrl)
                .build();

    }

}
