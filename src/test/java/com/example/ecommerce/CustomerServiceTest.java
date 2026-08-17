package com.example.ecommerce;

import com.example.ecommerce.dto.customer.CustomerRequestDto;
import com.example.ecommerce.dto.customer.CustomerResponseDto;
import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerRequestDto requestDto;

    @BeforeEach
    void setUp() {
        customer = new Customer("John Doe", "john@example.com", "9876543210", "123 Main St");
        customer.setId(1L);
        requestDto = new CustomerRequestDto("John Doe", "john@example.com", "9876543210", "123 Main St");
    }

    @Test
    void testCreateCustomer_Success() {
        when(customerRepository.findByEmail(requestDto.email())).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponseDto response = customerService.create(requestDto);

        assertNotNull(response);
        assertEquals("John Doe", response.name());
        assertEquals("john@example.com", response.email());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testCreateCustomer_DuplicateEmail() {
        when(customerRepository.findByEmail(requestDto.email())).thenReturn(Optional.of(customer));

        assertThrows(DuplicateResourceException.class, () -> customerService.create(requestDto));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testGetCustomerById_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerResponseDto response = customerService.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("John Doe", response.name());
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getById(99L));
    }

    @Test
    void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<CustomerResponseDto> list = customerService.getAll();

        assertEquals(1, list.size());
        assertEquals("John Doe", list.get(0).name());
    }
}
