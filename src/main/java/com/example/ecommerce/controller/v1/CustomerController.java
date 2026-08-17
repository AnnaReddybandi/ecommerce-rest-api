package com.example.ecommerce.controller.v1;

import com.example.ecommerce.dto.customer.CustomerRequestDto;
import com.example.ecommerce.dto.customer.CustomerResponseDto;
import com.example.ecommerce.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@Tag(
        name = "Customers",
        description = "Customer Management APIs"
)
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @Operation(summary = "Create a new customer")
    public ResponseEntity<CustomerResponseDto> create(
            @Valid @RequestBody CustomerRequestDto request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.create(request));
    }

    @GetMapping
    @Operation(summary = "Get all customers")
    public ResponseEntity<List<CustomerResponseDto>> getAll() {

        return ResponseEntity.ok(
                customerService.getAll()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    public ResponseEntity<CustomerResponseDto> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                customerService.getById(id)
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer details")
    public ResponseEntity<CustomerResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequestDto request) {

        return ResponseEntity.ok(
                customerService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer by ID")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        customerService.delete(id);

        return ResponseEntity.noContent().build();
    }
}