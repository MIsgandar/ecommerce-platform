package com.ecommerce.orderservice.client;

import com.ecommerce.orderservice.dto.ProductResponse;
import com.ecommerce.orderservice.exception.ProductNotFoundException;
import com.ecommerce.orderservice.exception.ProductServiceException;
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
                .onStatus(
                        status -> status.value() == 404,
                        response -> response.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .map(body -> new ProductNotFoundException(
                                        "Product not found: " + productId
                                ))
                )
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .map(body -> new ProductServiceException(
                                        "Product service returned client error: "
                                        + response.statusCode()
                                ))
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .map(body -> new ProductServiceException(
                                        "Product service returned server error: "
                                        + response.statusCode()
                                ))
                )
                .bodyToMono(ProductResponse.class)
                .block();

    }
}
