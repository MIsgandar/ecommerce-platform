package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.client.ProductClient;
import com.ecommerce.orderservice.dto.*;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderItem;
import com.ecommerce.orderservice.entity.OrderStatus;
import com.ecommerce.orderservice.entity.ProductStatus;
import com.ecommerce.orderservice.exception.InsufficientStockException;
import com.ecommerce.orderservice.exception.OrderNotFoundException;
import com.ecommerce.orderservice.exception.ProductNotAvailableException;
import com.ecommerce.orderservice.repository.OrderItemRepo;
import com.ecommerce.orderservice.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {


    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final ProductClient productClient;

    @Override
    public OrderResponse createOrder(
            UUID userId, CreateOrderRequest request) {

        List<OrderItem> orderItems  = new ArrayList<>();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for(CreateOrderItemRequest itemRequest : request.items()) {

            ProductResponse product =
                    productClient.getProduct(itemRequest.productId());

            System.out.println("PRODUCT RESPONSE: " + product);
            System.out.println("PRODUCT STATUS: [" + product.status() + "]");
            System.out.println("EXPECTED STATUS: [" + ProductStatus.ACTIVE.name() + "]");

            if (!ProductStatus.ACTIVE.name().equals(product.status())) {
                throw new ProductNotAvailableException(
                        "Product is not active: " + product.id()
                );
            }


            if(product.quantity() < itemRequest.quantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for product: " + product.id()
                );
            }

            BigDecimal subTotal = product.price()
                    .multiply(
                            BigDecimal.valueOf(itemRequest.quantity())
                    );

            totalAmount = totalAmount.add(subTotal);

            OrderItem orderItem = OrderItem.builder()
                    .productId(product.id())
                    .quantity(itemRequest.quantity())
                    .unitPrice(product.price())
                    .subTotal(subTotal)
                    .build();

            orderItems.add(orderItem);
        }

        Instant now = Instant.now();

        Order order = Order.builder()
                .userId(userId)
                .status(OrderStatus.PENDING)
                .totalAmount(totalAmount)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Order savedOrder = orderRepo.save(order);

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderId(savedOrder.getId());
        }

        orderItemRepo.saveAll(orderItems);

        List<OrderItemResponse> itemResponses = orderItems.stream()
                .map(this::mapToItemResponse)
                .toList();

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getStatus(),
                savedOrder.getTotalAmount(),
                itemResponses,
                savedOrder.getCreatedAt(),
                savedOrder.getUpdatedAt()
        );

    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(UUID orderId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(
                        "Order not found: " + orderId
                ));

        List<OrderItem> orderItems = orderItemRepo.findByOrderId(orderId);

        List<OrderItemResponse> itemResponses = orderItems.stream()
                .map(this::mapToItemResponse)
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getStatus(),
                order.getTotalAmount(),
                itemResponses,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );

    }

    private OrderItemResponse mapToItemResponse(OrderItem item) {

        return new OrderItemResponse(
                item.getId(),
                item.getProductId(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubTotal()
        );
    }



}
