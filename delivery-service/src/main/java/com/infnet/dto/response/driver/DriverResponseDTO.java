package com.infnet.dto.response.driver;

import com.infnet.domain.entity.enums.VehicleType;

import java.util.UUID;

public record DriverResponseDTO(

        UUID id,

        String name,

        String phone,

        VehicleType vehicleType,

        Double latitude,

        Double longitude,

        Boolean available
) {
}
