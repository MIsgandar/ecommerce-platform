package com.ecommerce.userservice.client;

import com.ecommerce.userservice.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductClientImpl implements ProductClient {

    private final WebClient webClient;

    @Override
    public ProductResponse getProduct(UUID productId) {

        return webClient
                .get()
                .uri("/api/products/{id}", productId)
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .block();

    }
}
