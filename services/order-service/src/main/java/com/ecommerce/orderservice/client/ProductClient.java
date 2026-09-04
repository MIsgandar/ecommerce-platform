package com.ecommerce.orderservice.client;

import com.ecommerce.orderservice.dto.ProductResponse;

import java.util.UUID;

public interface ProductClient {

    ProductResponse getProduct(UUID productId);

}
