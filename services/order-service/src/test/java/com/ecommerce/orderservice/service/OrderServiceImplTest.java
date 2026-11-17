package com.ecommerce.orderservice.service;


//Retrieve an existing order.
//
//Return an error when an order does not exist.
//
//Successfully cancel a pending order.
//
//Reject cancellation of a confirmed order.

import com.ecommerce.orderservice.client.ProductClient;
import com.ecommerce.orderservice.dto.CreateOrderItemRequest;
import com.ecommerce.orderservice.dto.CreateOrderRequest;
import com.ecommerce.orderservice.dto.ProductResponse;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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

    @Test
    void createOrderShouldCreateOrderSuccessfully() {

        UUID userId = UUID.randomUUID();
        UUID productID = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        ProductResponse product = createProduct(
                productID,
                "ACTIVE",
                10,
                new BigDecimal("50.00")
        );

        CreateOrderRequest request = new CreateOrderRequest(
                List.of(new CreateOrderItemRequest(
                        productID, 2
                ))
        );

        when(productClient.getProduct(productID))
                .thenReturn(product);

        when(orderRepo.save(any(Order.class)))
                .thenAnswer(invocation -> {

                        Order order = invocation.getArgument(0);
                        order.setId(orderId);
                        return  order;
                });

        when(orderItemRepo.saveAll(anyList()))
                .thenAnswer(
                        invocation -> invocation.getArgument(0));

        var response = orderService.createOrder(userId,request);

        assertNotNull(response);

        assertEquals(orderId, response.id());
        assertEquals(userId, response.userId());
        assertEquals(OrderStatus.PENDING, response.status());

        assertEquals(
                new BigDecimal("100.00"),
                response.totalAmount()
        );

        assertEquals(1, response.items().size());

        assertEquals(
                new BigDecimal("50.00"),
                response.items().get(0).unitPrice()
        );

        assertEquals(
                new BigDecimal("100.00"),
                response.items().get(0).subTotal()
        );

        verify(productClient).getProduct(productID);
        verify(orderRepo).save(any(Order.class));
        verify(orderItemRepo).saveAll(anyList());

    }

    @Test
    void createOrderShouldCalculateTotalForMultipleProducts() {

        UUID userId = UUID.randomUUID();

        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();

        UUID orderId = UUID.randomUUID();

        ProductResponse product1 = createProduct(

                productId1,
                "ACTIVE",
                10,
                new BigDecimal("50.00")
        );

        ProductResponse product2 = createProduct(

                productId2,
                "ACTIVE",
                10,
                new BigDecimal("20.00")
        );

        CreateOrderRequest request = new CreateOrderRequest(
                List.of(
                        new CreateOrderItemRequest(productId1,2),
                        new CreateOrderItemRequest(productId2,3))
        );

        when(productClient.getProduct(productId1))
                .thenReturn(product1);

        when(productClient.getProduct(productId2))
                .thenReturn(product2);

        when(orderRepo.save(any(Order.class)))
                .thenAnswer(invocation -> {

                    Order order = invocation.getArgument(0);
                    order.setId(orderId);
                    return order;

                });

        when(orderItemRepo.saveAll(anyList()))
                .thenAnswer(invocation ->

                        invocation.getArgument(0));

        var response = orderService.createOrder(userId, request);

        assertNotNull(response);

        assertEquals(orderId, response.id());
        assertEquals(2, response.items().size());

        assertEquals(
                new BigDecimal("160.00"),
                response.totalAmount()
        );

        assertEquals(
                new BigDecimal("100.00"),
                response.items().get(0).subTotal()
        );

        assertEquals(
                new BigDecimal("60.00"),
                response.items().get(1).subTotal()
        );

        verify(productClient).getProduct(productId1);
        verify(productClient).getProduct(productId2);

        verify(orderRepo).save(any(Order.class));
        verify(orderItemRepo).saveAll(anyList());

    }



    private ProductResponse createProduct(
            UUID productID,
            String status,
            int quantity,
            BigDecimal price
    ) {

        return new ProductResponse(
                productID,
                "IPHONE-16-BLK-256",
                "iPhone 16",
                "Apple smartphone",
                price,
                quantity,
                status,
                UUID.randomUUID(),
                "Electronics",
                Instant.now(),
                Instant.now()
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
