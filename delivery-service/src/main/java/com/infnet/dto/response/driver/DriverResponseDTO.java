package com.infnet.dto.response.driver;

import com.infnet.domain.entity.enums.VehicleType;

public record DriverResponseDTO(

        Long id,

        String name,

        String phone,

        VehicleType vehicleType,

        Double latitude,

        Double longitude,

        Boolean available
) {
}
