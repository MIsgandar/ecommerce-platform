package com.ecommerce.productservice.exception;

public class CategoryInUseException extends RuntimeException{

    public CategoryInUseException(String message) {
        super(message);
    }

}
