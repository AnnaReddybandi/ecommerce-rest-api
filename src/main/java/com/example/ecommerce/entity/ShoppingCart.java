package com.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "shopping_carts",
        indexes = {
                @Index(
                        name = "idx_shopping_cart_customer_id",
                        columnList = "customer_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart extends BaseEntity {

    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "customer_id",
            nullable = false
    )
    private Customer customer;

    @OneToMany(
            mappedBy = "shoppingCart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CartItem> cartItems = new ArrayList<>();
}