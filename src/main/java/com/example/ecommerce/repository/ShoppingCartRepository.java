package com.example.ecommerce.repository;

import com.example.ecommerce.entity.ShoppingCart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository
        extends JpaRepository<ShoppingCart, Long> {

    @Query("""
            SELECT sc
            FROM ShoppingCart sc
            WHERE sc.customer.id = :customerId
            """)
    Optional<ShoppingCart> findByCustomerId(
            @Param("customerId") Long customerId
    );

    @Query("""
            SELECT sc
            FROM ShoppingCart sc
            WHERE sc.updatedAt < :cutoffDate
            """)
    List<ShoppingCart> findAbandonedCarts(
            @Param("cutoffDate") LocalDateTime cutoffDate
    );

    @Query(
            value = """
                    SELECT sc.*
                    FROM shopping_carts sc
                    INNER JOIN cart_items ci
                        ON sc.id = ci.cart_id
                    GROUP BY sc.id
                    """,
            nativeQuery = true
    )
    List<ShoppingCart> findCartsWithItems();
}