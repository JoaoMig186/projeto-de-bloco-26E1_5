package com.infnet.dto.response.delivery;

import com.infnet.domain.entity.enums.DeliveryStatus;

import java.util.UUID;

public record DeliveryStatusResponseDTO(

        UUID deliveryId,

        DeliveryStatus status

) {
}