package com.ecommerce.productservice.exception;

public class CategoryNotFoundException extends RuntimeException{

    public CategoryNotFoundException(String messsage) {
        super(messsage);
    }
}
