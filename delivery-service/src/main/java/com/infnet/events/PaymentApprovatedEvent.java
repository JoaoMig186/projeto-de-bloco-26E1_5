package com.infnet.events;

public record PaymentApprovatedEvent(
        Long orderId,
        Boolean paymentApproved
) {}
