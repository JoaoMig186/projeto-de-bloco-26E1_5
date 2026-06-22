package com.infnet.events;

public record DeliveryUpdatedEvent(
        Long orderId,
        String deliveryStatus
) {}
