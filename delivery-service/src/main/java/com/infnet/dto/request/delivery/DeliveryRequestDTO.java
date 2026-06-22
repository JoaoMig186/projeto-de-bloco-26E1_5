package com.infnet.dto.request.delivery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DeliveryRequestDTO(

        @NotNull
        Long orderId,

        @NotBlank
        String originAddress,

        @NotBlank
        String destinationAddress,

        @Positive
        Double distanceKm,

        @Positive
        Double weightKg

) {
}
