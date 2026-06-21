package com.infnet.kafka.events;

import com.infnet.domain.enums.PaymentMethod;

import java.math.BigDecimal;

public record OrderCreatedEvent(
        Long orderId,
        Long storeId,
        Long userId,
        BigDecimal totalprice,
        PaymentMethod paymentMethod
) {
}
