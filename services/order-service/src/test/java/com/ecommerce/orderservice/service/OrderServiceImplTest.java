package com.ecommerce.orderservice.service;


//Retrieve an existing order.
//
//Return an error when an order does not exist.
//
//Successfully cancel a pending order.
//
//Reject cancellation of a confirmed order.

import com.ecommerce.orderservice.client.ProductClient;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderStatus;
import com.ecommerce.orderservice.exception.InvalidOrderStatusTransitionException;
import com.ecommerce.orderservice.exception.OrderNotFoundException;
import com.ecommerce.orderservice.repository.OrderItemRepo;
import com.ecommerce.orderservice.repository.OrderRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private OrderItemRepo orderItemRepo;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void getOrderShouldReturnExistingOrder() {

        UUID orderId = UUID.randomUUID();

        Order order = createOrder(orderId, OrderStatus.PENDING);

        when(orderRepo.findById(orderId))
                .thenReturn(Optional.of(order));

        when(orderItemRepo.findByOrderId(orderId))
                .thenReturn(List.of());

        var response = orderService.getOrder(orderId);

        assertEquals(orderId, response.id());
        assertEquals(OrderStatus.PENDING, response.status());
        assertEquals(
                new BigDecimal("100.00"),
                response.totalAmount()
        );

        verify(orderRepo).findById(orderId);

    }

    @Test
    void getOrderShouldThrowWhenOrderDoesNotExist() {

        UUID orderId = UUID.randomUUID();

        when(orderRepo.findById(orderId))
                .thenReturn(Optional.empty());

        assertThrows(
                OrderNotFoundException.class,
                () -> orderService.getOrder(orderId)
        );

    }

    @Test
    void cancelOrderShouldCancelPendingOrder() {
        UUID orderId = UUID.randomUUID();
        Order order = createOrder(orderId, OrderStatus.PENDING);

        when(orderRepo.findById(orderId))
                .thenReturn(Optional.of(order));

        when(orderRepo.save(any(Order.class)))
                .thenAnswer(
                        invocation -> invocation.getArgument(0));

        when(orderItemRepo.findByOrderId(orderId))
                .thenReturn(java.util.List.of());

        var response = orderService.cancelOrder(orderId);

        assertEquals(OrderStatus.CANCELLED, response.status());
        assertEquals(OrderStatus.CANCELLED, order.getStatus());

        verify(orderRepo).save(order);
    }

    @Test
    void cancelOrderShouldRejectConfirmedOrder() {
        UUID orderId = UUID.randomUUID();
        Order order = createOrder(orderId, OrderStatus.CONFIRMED);

        when(orderRepo.findById(orderId))
                .thenReturn(Optional.of(order));

        assertThrows(
                InvalidOrderStatusTransitionException.class,
                () -> orderService.cancelOrder(orderId)
        );
    }

    private Order createOrder(UUID orderId, OrderStatus status) {

        Instant now = Instant.now();

        return Order.builder()
                .id(orderId)
                .userId(UUID.randomUUID())
                .status(status)
                .totalAmount(new BigDecimal("100.00"))
                .createdAt(now)
                .updatedAt(now)
                .build();

    }

}
