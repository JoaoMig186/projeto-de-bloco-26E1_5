package com.infnet.delivery_sevice.dto.request;

import com.infnet.delivery_sevice.domain.entity.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DriverRequestDTO(
        @NotBlank
        String name,
        @NotBlank
        String phone,
        @NotNull
        VehicleType vehicleType
) {
}
