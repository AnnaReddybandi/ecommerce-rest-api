package com.example.ecommerce.service;

import com.example.ecommerce.dto.cart.ShoppingCartRequestDto;
import com.example.ecommerce.dto.cart.ShoppingCartResponseDto;

import java.util.List;

public interface ShoppingCartService {

    ShoppingCartResponseDto create(ShoppingCartRequestDto request);

    ShoppingCartResponseDto getById(Long id);

    List<ShoppingCartResponseDto> getAll();

    void delete(Long id);

    ShoppingCartResponseDto getByCustomerId(Long customerId);

    ShoppingCartResponseDto clearCart(Long cartId);
}