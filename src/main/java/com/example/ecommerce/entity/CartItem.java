package com.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(
        name = "cart_items",
        indexes = {
                @Index(name = "idx_cart_id", columnList = "cart_id"),
                @Index(name = "idx_product_id", columnList = "product_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem extends BaseEntity {

    @NotNull(message = "Shopping cart is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private ShoppingCart shoppingCart;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(nullable = false)
    private Integer quantity;
}