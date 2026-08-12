package com.ecommerce.productservice.dto;

import com.ecommerce.productservice.entity.ProductStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductResponse(

        UUID id,

        String sku,

        String name,

        String description,

        BigDecimal price,

        Integer quantity,

        ProductStatus status,

        UUID categoryId,

        String categoryName,

        Instant createdAt,

        Instant updatedAt
)
{

}