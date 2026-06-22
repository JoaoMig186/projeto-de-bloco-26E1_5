package com.infnet.kafka.events;

public record DeliveryUpdatedEvent(
        Long orderId,
        String deliveryStatus
) {
}
