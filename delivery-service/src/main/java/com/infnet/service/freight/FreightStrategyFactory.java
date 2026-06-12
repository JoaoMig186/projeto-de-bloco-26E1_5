package com.infnet.service.freight;

import com.infnet.domain.entity.enums.VehicleType;
import com.infnet.service.freight.strategy.FreightStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FreightStrategyFactory {

    private final Map<VehicleType, FreightStrategy> strategies;

    public FreightStrategyFactory(
            List<FreightStrategy> strategies
    ) {

        this.strategies =
                strategies.stream()
                        .collect(Collectors.toMap(
                                FreightStrategy::supportedVehicle,
                                Function.identity()
                        ));
    }

    public FreightStrategy getStrategy(
            VehicleType vehicleType
    ) {

        FreightStrategy strategy =
                strategies.get(vehicleType);

        if(strategy == null) {
            throw new IllegalArgumentException(
                    "Estratégia não encontrada."
            );
        }

        return strategy;
    }
}
