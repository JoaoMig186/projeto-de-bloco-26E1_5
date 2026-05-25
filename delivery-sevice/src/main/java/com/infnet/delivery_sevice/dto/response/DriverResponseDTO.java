package com.infnet.delivery_sevice.dto.response;

import com.infnet.delivery_sevice.domain.entity.enums.VehicleType;

import java.util.UUID;

public record DriverResponseDTO(
        UUID id,
        String name,
        VehicleType vehicleType,
        Boolean available
) {
}
