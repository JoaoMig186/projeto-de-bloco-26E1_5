package com.infnet.dto.request.Freight;

import com.infnet.domain.entity.enums.VehicleType;

public record FreightRequestDTO(
        Double distanceKm,
        Double weightKg
) {
}
