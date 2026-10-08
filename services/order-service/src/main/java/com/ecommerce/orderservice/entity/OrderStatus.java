package com.ecommerce.orderservice.entity;

public enum OrderStatus {

    PENDING,

    CONFIRMED,

    CANCELLED,

    COMPLETED;

    public boolean canTransitionTo(OrderStatus newStatus) {

        return switch (this) {

            case PENDING ->
                newStatus == CONFIRMED || newStatus == CANCELLED;

            case CONFIRMED ->
                newStatus == COMPLETED;

            case CANCELLED, COMPLETED -> false;
        };

    }

}
