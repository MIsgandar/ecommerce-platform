package com.ecommerce.orderservice.controller;


import com.ecommerce.orderservice.dto.CreateOrderRequest;
import com.ecommerce.orderservice.dto.OrderResponse;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestParam UUID userId,
            @Valid @RequestBody CreateOrderRequest request
            )
    {

        OrderResponse response = orderService.createOrder(userId, request);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable UUID orderId
    ) {

        OrderResponse response = orderService.getOrder(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<OrderResponse>> getOrderByUser(
            @PathVariable UUID userId,
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )Pageable pageable

            ) {

        Page<OrderResponse> orders = orderService.getOrderByUser(userId, pageable);

        return ResponseEntity.ok(orders);

    }

}
