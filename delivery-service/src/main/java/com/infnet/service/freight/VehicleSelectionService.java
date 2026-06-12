package com.infnet.service.freight;

import com.infnet.domain.entity.enums.VehicleType;
import org.springframework.stereotype.Service;

@Service
public class VehicleSelectionService {

    public VehicleType selectVehicle(double weightKg) {

        for (VehicleType vehicleType : VehicleType.values()) {

            if (weightKg <= vehicleType.getMaxWeight()) {
                return vehicleType;
            }
        }

        throw new IllegalArgumentException(
                "Nenhum veículo suporta o peso informado."
        );
    }
}
