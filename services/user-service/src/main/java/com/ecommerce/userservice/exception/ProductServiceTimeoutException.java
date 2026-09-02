package com.ecommerce.userservice.exception;

public class ProductServiceTimeoutException extends RuntimeException {


    public ProductServiceTimeoutException(String message) {
        super(message);
    }

    public ProductServiceTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
