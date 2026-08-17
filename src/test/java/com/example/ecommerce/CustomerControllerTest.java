package com.example.ecommerce;

import com.example.ecommerce.controller.v1.CustomerController;
import com.example.ecommerce.dto.customer.CustomerRequestDto;
import com.example.ecommerce.dto.customer.CustomerResponseDto;
import com.example.ecommerce.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateCustomer_Success() throws Exception {
        CustomerRequestDto request = new CustomerRequestDto("Anna Reddy", "anna@example.com", "9876543210", "Bangalore");
        CustomerResponseDto response = new CustomerResponseDto(1L, "Anna Reddy", "anna@example.com", "9876543210", "Bangalore", LocalDateTime.now(), LocalDateTime.now());

        when(customerService.create(any(CustomerRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Anna Reddy"))
                .andExpect(jsonPath("$.email").value("anna@example.com"));
    }

    @Test
    void testCreateCustomer_ValidationError() throws Exception {
        CustomerRequestDto invalidRequest = new CustomerRequestDto("", "invalid-email", "123", "");

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCustomerById() throws Exception {
        CustomerResponseDto response = new CustomerResponseDto(1L, "Anna Reddy", "anna@example.com", "9876543210", "Bangalore", LocalDateTime.now(), LocalDateTime.now());

        when(customerService.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Anna Reddy"));
    }

    @Test
    void testGetAllCustomers() throws Exception {
        CustomerResponseDto response = new CustomerResponseDto(1L, "Anna Reddy", "anna@example.com", "9876543210", "Bangalore", LocalDateTime.now(), LocalDateTime.now());

        when(customerService.getAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Anna Reddy"));
    }
}
