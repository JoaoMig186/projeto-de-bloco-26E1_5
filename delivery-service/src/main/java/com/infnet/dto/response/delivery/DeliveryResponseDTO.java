package com.infnet.dto.response.delivery;

import com.infnet.domain.entity.enums.DeliveryStatus;

import java.time.LocalDateTime;

public record DeliveryResponseDTO(

    Long id,

    Long orderId,

    Long driverId,

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