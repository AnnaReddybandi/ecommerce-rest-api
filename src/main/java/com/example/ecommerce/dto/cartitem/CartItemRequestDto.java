package com.example.ecommerce.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequestDto(

        @NotNull(message = "Shopping cart ID is required")
        Long cartId,

        @NotNull(message = "Product ID is required")
        Long productId,

        @NotNull(message = "Quantity is required")
        @Min(
                value = 1,
                message = "Quantity must be at least 1"
        )
        Integer quantity
) {
}