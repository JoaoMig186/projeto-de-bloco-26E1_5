package com.infnet.service.freight.strategy;

import com.infnet.domain.entity.enums.VehicleType;
import com.infnet.dto.request.freight.FreightResponseDTO;

public interface FreightStrategy {

    VehicleType supportedVehicle();

    FreightResponseDTO calculate(
            Double distanceKm,
            Double weightKg
    );
}