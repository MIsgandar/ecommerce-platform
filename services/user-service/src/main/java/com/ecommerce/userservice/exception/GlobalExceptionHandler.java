package com.ecommerce.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductServiceException.class)
    public ResponseEntity<Map<String, Object>> handleProductServiceException(
            ProductServiceException exception
    ) {

        Map<String, Object> body = Map.of(
                "timestamp", Instant.now(),
                "status", HttpStatus.BAD_GATEWAY.value(),
                "error", HttpStatus.BAD_GATEWAY.getReasonPhrase(),
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(body);

    }

    @ExceptionHandler(ProductServiceTimeoutException.class)
    public ResponseEntity<Map<String, Object>> handleProductServiceTimeout(
            ProductServiceTimeoutException exception
    ) {

        return ResponseEntity
                .status((HttpStatus.GATEWAY_TIMEOUT))
                .body(Map.of(
                        "timestamp", Instant.now(),
                        "status", HttpStatus.GATEWAY_TIMEOUT.value(),
                        "error", HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase(),
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(ProductServiceUnavailableException.class)
    public ResponseEntity<Map<String, Object>> handleProductServiceUnavailable(
            ProductServiceUnavailableException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "timestamp", Instant.now(),
                        "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                        "error", HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
                        "message", exception.getMessage()
                ));
    }

}
