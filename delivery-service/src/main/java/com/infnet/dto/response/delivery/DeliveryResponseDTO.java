package com.infnet.dto.response.delivery;

import com.infnet.domain.entity.enums.DeliveryStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryResponseDTO(

    UUID id,

    UUID orderId,

    UUID driverId,

    String originAddress,

    String destinationAddress,

    Double distanceKm,

    Integer estimatedTimeMinutes,

    Double shippingPrice,

    DeliveryStatus status,

    LocalDateTime createdAt,

    LocalDateTime deliveredAt

) {
}