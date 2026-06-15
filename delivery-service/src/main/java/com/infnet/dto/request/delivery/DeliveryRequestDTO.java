package com.infnet.dto.request.delivery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record DeliveryRequestDTO(

        @NotNull
        UUID orderId,

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
