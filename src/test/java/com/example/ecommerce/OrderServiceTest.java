package com.example.ecommerce;

import com.example.ecommerce.dto.order.CheckoutRequestDto;
import com.example.ecommerce.dto.order.OrderRequestDto;
import com.example.ecommerce.dto.order.OrderResponseDto;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.service.impl.OrderServiceImpl;
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
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Customer customer;
    private Order order;
    private OrderRequestDto requestDto;
    private Product product;
    private ShoppingCart cart;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        customer = new Customer("John Doe", "john@example.com", "9876543210", "123 Main St");
        customer.setId(1L);

        product = new Product("Keyboard", "RGB Keyboard", new BigDecimal("1000.00"), 10, ProductCategory.ELECTRONICS);
        product.setId(1L);

        cart = new ShoppingCart(customer);
        cart.setId(1L);

        cartItem = new CartItem(cart, product, 2);
        cartItem.setId(1L);

        order = new Order(customer, "123 Main St", new BigDecimal("2000.00"));
        order.setId(1L);

        requestDto = new OrderRequestDto(1L, "123 Main St", new BigDecimal("2000.00"));
    }

    @Test
    void testCreateOrder_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponseDto response = orderService.create(requestDto);

        assertNotNull(response);
        assertEquals(1L, response.customerId());
        assertEquals(OrderStatus.PENDING, response.status());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testConfirmOrder_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponseDto response = orderService.confirmOrder(1L);

        assertEquals(OrderStatus.CONFIRMED, response.status());
    }

    @Test
    void testCancelOrder_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponseDto response = orderService.cancelOrder(1L);

        assertEquals(OrderStatus.CANCELLED, response.status());
    }

    @Test
    void testCancelOrder_AlreadyDelivered() {
        order.setStatus(OrderStatus.DELIVERED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderException.class, () -> orderService.cancelOrder(1L));
    }

    @Test
    void testCheckout_Success() {
        CheckoutRequestDto checkoutRequest = new CheckoutRequestDto(1L, "123 Main St", PaymentMethod.UPI);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(shoppingCartRepository.findByCustomerId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(1L)).thenReturn(List.of(cartItem));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponseDto response = orderService.checkout(checkoutRequest);

        assertNotNull(response);
        assertEquals(1L, response.id());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(cartItemRepository, times(1)).deleteByCartId(1L);
    }

    @Test
    void testCheckout_InsufficientStock() {
        CheckoutRequestDto checkoutRequest = new CheckoutRequestDto(1L, "123 Main St", PaymentMethod.UPI);
        product.setStock(1); // available is 1, but cart requested 2

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(shoppingCartRepository.findByCustomerId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(1L)).thenReturn(List.of(cartItem));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(InsufficientStockException.class, () -> orderService.checkout(checkoutRequest));
    }
}
