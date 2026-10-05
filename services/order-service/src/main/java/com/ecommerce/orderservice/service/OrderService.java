package com.ecommerce.orderservice.service;


import com.ecommerce.orderservice.dto.CreateOrderRequest;
import com.ecommerce.orderservice.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {

    OrderResponse createOrder(UUID userId, CreateOrderRequest request);

    OrderResponse getOrder(UUID orderId);

    Page<OrderResponse> getOrderByUser(UUID userId, Pageable pageable);

}
