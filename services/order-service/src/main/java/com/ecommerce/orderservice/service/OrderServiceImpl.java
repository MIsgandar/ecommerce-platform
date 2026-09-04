package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.CreateOrderRequest;
import com.ecommerce.orderservice.dto.OrderResponse;
import com.ecommerce.orderservice.repository.OrderItemRepo;
import com.ecommerce.orderservice.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {


    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;

    @Override
    public OrderResponse createOrder(
            UUID id, CreateOrderRequest request) {

        return null;
    }



}
