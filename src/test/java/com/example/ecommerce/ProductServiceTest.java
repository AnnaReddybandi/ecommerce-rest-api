package com.example.ecommerce;

import com.example.ecommerce.dto.product.ProductRequestDto;
import com.example.ecommerce.dto.product.ProductResponseDto;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.ProductCategory;
import com.example.ecommerce.entity.ProductStatus;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.impl.ProductServiceImpl;
import com.example.ecommerce.util.FileStorageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private FileStorageUtil fileStorageUtil;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequestDto requestDto;

    @BeforeEach
    void setUp() {
        product = new Product("Mechanical Keyboard", "RGB Gaming Keyboard", new BigDecimal("2499.00"), 50, ProductCategory.ELECTRONICS);
        product.setId(1L);
        requestDto = new ProductRequestDto("Mechanical Keyboard", "RGB Gaming Keyboard", new BigDecimal("2499.00"), 50, ProductCategory.ELECTRONICS);
    }

    @Test
    void testCreateProduct_Success() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDto response = productService.create(requestDto);

        assertNotNull(response);
        assertEquals("Mechanical Keyboard", response.name());
        assertEquals(50, response.stock());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponseDto response = productService.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Mechanical Keyboard", response.name());
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getById(99L));
    }

    @Test
    void testReduceStock_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponseDto response = productService.reduceStock(1L, 10);

        assertEquals(40, response.stock());
    }

    @Test
    void testReduceStock_Insufficient() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(InsufficientStockException.class, () -> productService.reduceStock(1L, 100));
    }

    @Test
    void testIncreaseStock_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponseDto response = productService.increaseStock(1L, 20);

        assertEquals(70, response.stock());
    }

    @Test
    void testGetByPriceRange() {
        when(productRepository.findByPriceRange(new BigDecimal("1000"), new BigDecimal("3000"), ProductStatus.ACTIVE))
                .thenReturn(List.of(product));

        List<ProductResponseDto> list = productService.getByPriceRange(new BigDecimal("1000"), new BigDecimal("3000"));

        assertEquals(1, list.size());
        assertEquals("Mechanical Keyboard", list.get(0).name());
    }
}
