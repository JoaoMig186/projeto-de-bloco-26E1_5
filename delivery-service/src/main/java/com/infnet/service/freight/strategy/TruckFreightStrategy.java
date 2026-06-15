package com.infnet.service.freight.strategy;

import com.infnet.domain.entity.enums.VehicleType;
import com.infnet.dto.request.freight.FreightResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class TruckFreightStrategy
        implements FreightStrategy {

    private static final double BASE_FEE = 30.0;
    private static final double PRICE_PER_KM = 4.5;
    private static final double AVG_SPEED = 30.0;
    private static final Integer MINUTES_PER_HOUR = 60;

    @Override
    public VehicleType supportedVehicle() {
        return VehicleType.TRUCK;
    }

    @Override
    public FreightResponseDTO calculate(
            Double distanceKm,
            Double weightKg
    ) {

        double freight =
                BASE_FEE +
                        (distanceKm * PRICE_PER_KM);

        if(weightKg > 300) {
            freight += 50;
        }

        int eta =
                (int) ((distanceKm / AVG_SPEED) * MINUTES_PER_HOUR);

        return new FreightResponseDTO(
                supportedVehicle(),
                freight,
                eta
        );
    }
}