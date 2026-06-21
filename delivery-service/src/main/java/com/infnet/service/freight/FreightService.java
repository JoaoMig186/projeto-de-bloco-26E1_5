package com.infnet.service.freight;

import com.infnet.domain.entity.enums.VehicleType;
import com.infnet.dto.request.freight.FreightRequestDTO;
import com.infnet.dto.request.freight.FreightResponseDTO;
import com.infnet.exception.BusinessException;
import com.infnet.service.freight.strategy.FreightStrategy;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FreightService {

    private final VehicleSelectionService vehicleSelectionService;
    private final FreightStrategyFactory strategyFactory;

    @Retry(name = "freightService")
    @CircuitBreaker(
            name = "freightService",
            fallbackMethod = "calculateFallback"
    )
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

    @Retry(name = "freightService")
    @CircuitBreaker(
            name = "freightService",
            fallbackMethod = "calculateFallback"
    )
    public FreightResponseDTO forceErrorCalculateFreight(
            FreightRequestDTO dto
    ) {
        throw new RuntimeException("Simulated failure");
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
    private FreightResponseDTO calculateFallback(
            FreightRequestDTO dto,
            Exception ex
    ) {

        throw new BusinessException(
                "Serviço de cálculo de frete indisponível no momento.",
                ex
        );
    }
}
