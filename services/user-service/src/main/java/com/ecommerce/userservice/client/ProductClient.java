package com.ecommerce.userservice.client;

import com.ecommerce.userservice.dto.ProductResponse;

import java.util.UUID;

public interface ProductClient {

    ProductResponse getProduct(UUID productID);

}
