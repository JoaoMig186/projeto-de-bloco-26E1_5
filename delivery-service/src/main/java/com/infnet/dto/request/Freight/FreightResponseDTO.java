package com.infnet.dto.request.Freight;

import com.infnet.domain.entity.enums.VehicleType;

public record FreightResponseDTO(
        VehicleType vehicleType,
        Double freightValue,
        Integer estimatedMinutes
) {
}
