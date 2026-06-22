package com.infnet.kafka.events;

public record PaymentApprovatedEvent(
        Long orderId,
        Boolean paymentApproved
) {
}
