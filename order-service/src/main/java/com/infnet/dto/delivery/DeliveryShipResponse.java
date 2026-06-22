package com.infnet.dto.delivery;

import java.math.BigDecimal;

public record DeliveryShipResponse(
        String vehicleType,
        BigDecimal freightValue,
        Integer estimatedMinutes
) {
}
