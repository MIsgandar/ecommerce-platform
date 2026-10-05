package com.ecommerce.orderservice.repository;

import com.ecommerce.orderservice.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepo extends JpaRepository<Order, UUID> {

    Page<Order> findByUserId(UUID userId, Pageable pageable);
}
