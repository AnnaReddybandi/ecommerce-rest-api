package com.example.ecommerce.controller.v1;

import com.example.ecommerce.dto.cart.ShoppingCartRequestDto;
import com.example.ecommerce.dto.cart.ShoppingCartResponseDto;
import com.example.ecommerce.service.ShoppingCartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shopping-carts")
@Tag(
        name = "Shopping Carts",
        description = "Shopping Cart Management APIs"
)
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(
            ShoppingCartService shoppingCartService) {

        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping
    @Operation(summary = "Create a shopping cart")
    public ResponseEntity<ShoppingCartResponseDto> create(
            @Valid @RequestBody ShoppingCartRequestDto request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(shoppingCartService.create(request));
    }

    @GetMapping
    @Operation(summary = "Get all shopping carts")
    public ResponseEntity<List<ShoppingCartResponseDto>> getAll() {

        return ResponseEntity.ok(
                shoppingCartService.getAll()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get shopping cart by ID")
    public ResponseEntity<ShoppingCartResponseDto> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                shoppingCartService.getById(id)
        );
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get shopping cart by customer ID")
    public ResponseEntity<ShoppingCartResponseDto> getByCustomerId(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                shoppingCartService.getByCustomerId(customerId)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete shopping cart by ID")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        shoppingCartService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/clear")
    @Operation(summary = "Clear all items from a shopping cart")
    public ResponseEntity<ShoppingCartResponseDto> clearCart(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                shoppingCartService.clearCart(id)
        );
    }
}