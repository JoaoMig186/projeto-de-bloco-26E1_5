package com.infnet.events;

import java.math.BigDecimal;

public record PaymentApprovatedEvent(
        Long orderId,
        Boolean paymentApproved,
        Double distanceKm,
        Integer estimatedMinutes,
        BigDecimal shippingPrice
) {}
