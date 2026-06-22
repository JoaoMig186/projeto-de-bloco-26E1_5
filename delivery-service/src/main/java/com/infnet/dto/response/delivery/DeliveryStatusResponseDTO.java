package com.infnet.dto.response.delivery;

import com.infnet.domain.entity.enums.DeliveryStatus;

public record DeliveryStatusResponseDTO(

        Long deliveryId,

        DeliveryStatus status

) {
}