package com.infnet.service.freight;

import com.infnet.domain.entity.enums.VehicleType;
import com.infnet.dto.request.Freight.FreightRequestDTO;
import com.infnet.dto.request.Freight.FreightResponseDTO;
import com.infnet.service.freight.strategy.FreightStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FreightService {

    private final VehicleSelectionService vehicleSelectionService;
    private final FreightStrategyFactory strategyFactory;

    public FreightResponseDTO calculate(
            FreightRequestDTO dto
    ) {

        validateRequest(dto);

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

    private void validateRequest(
            FreightRequestDTO dto
    ) {

        if(dto.distanceKm() <= 0) {
            throw new IllegalArgumentException(
                    "Distância deve ser maior que zero."
            );
        }

        if(dto.weightKg() <= 0) {
            throw new IllegalArgumentException(
                    "Peso deve ser maior que zero."
            );
        }
    }
}
