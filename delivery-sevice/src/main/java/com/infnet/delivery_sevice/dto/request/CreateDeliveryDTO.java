package com.infnet.delivery_sevice.dto.request;

import java.util.UUID;

public record CreateDeliveryDTO(
        UUID orderId,
        String originAddress,
        String destinationAddress,
        Double distanceKm,
        Double weight
) {
}
