package com.infnet.dto.request.driver;

import com.infnet.domain.entity.enums.VehicleType;
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
