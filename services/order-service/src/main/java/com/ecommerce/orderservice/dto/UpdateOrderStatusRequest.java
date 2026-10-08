package com.ecommerce.orderservice.dto;

import com.ecommerce.orderservice.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;


public record UpdateOrderStatusRequest (

    @NotNull(message = "Order status is required")
    OrderStatus status
)
{

}

