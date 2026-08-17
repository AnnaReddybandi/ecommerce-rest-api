package com.example.ecommerce.repository;

import com.example.ecommerce.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByShoppingCartId(Long cartId);

    void deleteByShoppingCartId(Long cartId);

    boolean existsByShoppingCartIdAndProductId(
            Long cartId,
            Long productId
    );

    Optional<CartItem> findByShoppingCartIdAndProductId(
            Long cartId,
            Long productId
    );

    void deleteByCartId(Long cartId);
}