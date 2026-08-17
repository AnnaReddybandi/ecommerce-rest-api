package com.example.ecommerce.dto.orderitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequestDto(

        @NotNull(message = "Order ID is required")
        Long orderId,

        @NotNull(message = "Product ID is required")
        Long productId,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity

) {
}