package com.ecommerce.orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;


public record CreateOrderRequest(


        @NotEmpty(message = "Order must contain at least one item")
        List<@Valid CreateOrderItemRequest> items


        )
{

}
