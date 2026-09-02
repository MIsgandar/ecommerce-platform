package com.ecommerce.userservice.client;

import com.ecommerce.userservice.dto.ProductResponse;
import com.ecommerce.userservice.exception.ProductServiceException;
import com.ecommerce.userservice.exception.ProductServiceTimeoutException;
import com.ecommerce.userservice.exception.ProductServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
public class ProductClientImpl implements ProductClient {

    private final WebClient webClient;

    @Override
    public ProductResponse getProduct(UUID productId) {

        try {


            return webClient
                    .get()
                    .uri("/api/products/{id}", productId)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            response -> Mono.error(
                                    new ProductServiceException(
                                            "Product service returned client error: "
                                                    + response.statusCode()
                                    )
                            )
                    )
                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            response -> Mono.error(
                                    new ProductServiceException(
                                            "Product service returned server error: "
                                                    + response.statusCode()
                                    )
                            )
                    )
                    .bodyToMono(ProductResponse.class)
                    .block();

        } catch (WebClientRequestException exception) {

            if (exception.getCause() instanceof TimeoutException) {

                throw new ProductServiceTimeoutException(
                        "Product service request timed out",
                        exception);
            }

            if (exception.getCause() instanceof ConnectException) {

                throw new ProductServiceUnavailableException(
                        "Product service in unavailable",
                        exception
                );
            }

            throw new ProductServiceUnavailableException(
                    "Could not connect to product service",
                    exception
            );

        }

    }
}
