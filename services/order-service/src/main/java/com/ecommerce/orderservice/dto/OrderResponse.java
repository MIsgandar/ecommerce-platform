package com.ecommerce.orderservice.dto;

import com.ecommerce.orderservice.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(

        UUID id,

        UUID userId,

        OrderStatus status,

        BigDecimal totalAmount,

        List<OrderItemResponse> items,

        Instant createdAt,

        Instant updatedAt

) {
}
