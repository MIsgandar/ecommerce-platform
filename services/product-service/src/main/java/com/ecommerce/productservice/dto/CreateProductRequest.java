package com.ecommerce.productservice.dto;

import com.ecommerce.productservice.entity.ProductStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;


public record CreateProductRequest (

        @NotBlank(message = "SKU is required")
        @Size(max = 100, message = "SKU must not exceed 100 characters")
        String sku,

        @NotBlank(message = "Product name is required")
        @Size(max = 255, message = "Product name must not exceed 255 characters")
        String name,

        @Size(max = 2000, message = "Description must not exceed 2000 characters")
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(
                value = "0.01",
                message = "Price must be greater than zero"
        )
        @Digits(
                integer = 15,
                fraction = 4,
                message = "Invalid price format"
        )
        BigDecimal price,

        @NotNull(message = "Quantity is required")
        @Min(
                value = 0,
                message = "Quantity cannot be negative"
        )
        Integer quantity,

        @NotNull(message = "Category ID is required")
        UUID categoryId,

        ProductStatus productStatus
)
{

}
