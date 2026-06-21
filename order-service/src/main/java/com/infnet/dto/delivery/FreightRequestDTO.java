package com.infnet.dto.delivery;

import java.math.BigDecimal;

public record FreightRequestDTO(
        Double distanceKm,
        BigDecimal weightKg
) {
}
