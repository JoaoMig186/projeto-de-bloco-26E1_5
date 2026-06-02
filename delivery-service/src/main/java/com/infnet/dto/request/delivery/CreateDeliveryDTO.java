package com.infnet.dto.request.delivery;

import java.util.UUID;

public record CreateDeliveryDTO(
        UUID orderId,
        String originAddress,
        String destinationAddress,
        Double distanceKm,
        Double weight
) {
}
