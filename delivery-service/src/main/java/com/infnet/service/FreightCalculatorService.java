package com.infnet.service;

import com.infnet.domain.entity.enums.VehicleType;
import com.infnet.dto.request.Freight.FreightRequestDTO;
import com.infnet.dto.request.Freight.FreightResponseDTO;
import com.infnet.service.freight.FreightStrategyFactory;
import com.infnet.service.freight.VehicleSelectionService;
import com.infnet.service.freight.strategy.FreightStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FreightCalculatorService {

    private final VehicleSelectionService vehicleSelectionService;
    private final FreightStrategyFactory strategyFactory;

    public FreightResponseDTO calculate(
            FreightRequestDTO dto
    ) {
        VehicleType vehicleType =
                vehicleSelectionService.selectVehicle(
                        dto.weightKg()
                );

        FreightStrategy strategy =
                strategyFactory.getStrategy(
                        vehicleType
                );

        return strategy.calculate(
                dto.distanceKm(),
                dto.weightKg()
        );
    }
}