package com.infnet.kafka.events;

import com.infnet.domain.enums.PaymentStatus;

public record PaymentUpdatedEvent(
        Long orderId,
        String paymentStatus
) {
}
