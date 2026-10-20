package com.ecommerce.orderservice.entity;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class OrderStatusTest {

    @Test
    void pendingOrderCanBeConfirmed() {

        assertTrue(
                OrderStatus.PENDING.canTransitionTo(
                        OrderStatus.CONFIRMED
                )
        );

    }

    @Test
    void pendingOrderCanBeCanceled() {

        assertTrue(
                OrderStatus.PENDING.canTransitionTo(
                        OrderStatus.CANCELLED
                )
        );
    }

    @Test
    void confirmedOrderCanBeCompleted() {

        assertTrue(
                OrderStatus.CONFIRMED.canTransitionTo(
                        OrderStatus.COMPLETED
                )
        );
    }

    @Test
    void pendingOrderCannotBeCompletedDirectly() {

        assertFalse(
                OrderStatus.PENDING.canTransitionTo(
                        OrderStatus.COMPLETED
                )
        );

    }

    @Test
    void canceledOrderCannotTransitionToAnotherState() {

        assertFalse(
                OrderStatus.CANCELLED.canTransitionTo(
                        OrderStatus.PENDING
                )
        );

        assertFalse(
                OrderStatus.CANCELLED.canTransitionTo(
                        OrderStatus.CONFIRMED
                )
        );

        assertFalse(
                OrderStatus.CANCELLED.canTransitionTo(
                        OrderStatus.COMPLETED
                )
        );

    }

    @Test
    void completedOrderCannotTransitionToAnotherState() {

        assertFalse(
                OrderStatus.COMPLETED.canTransitionTo(
                        OrderStatus.PENDING
                )
        );

        assertFalse(
                OrderStatus.COMPLETED.canTransitionTo(
                        OrderStatus.CONFIRMED
                )
        );

        assertFalse(
                OrderStatus.COMPLETED.canTransitionTo(
                        OrderStatus.CANCELLED
                )
        );
    }

    @Test
    void orderCannotTransitionToItsCurrentState() {

        for (OrderStatus status : OrderStatus.values()) {

            assertFalse(status.canTransitionTo(status));
        }
    }

}

