package com.infnet.service.delivery;

import com.infnet.client.DeliveryIClient;
import com.infnet.dto.delivery.DeliveryShipResponse;
import com.infnet.dto.delivery.FreightRequestDTO;
import com.infnet.excepton.DeliveryServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Profile({"prod"})
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryIClient client;

    //    CircuitBreaker
    //    Retry
    @Override
    @CircuitBreaker(name = "deliveryService", fallbackMethod = "deliveryFallback")
    @Retry(name = "deliveryService")
    public DeliveryShipResponse getDeliveryPrice(FreightRequestDTO dto) {
        return client.getDeliveryPrice(dto);
    }
    private DeliveryShipResponse deliveryFallback(FreightRequestDTO dto, Throwable throwable) {
        System.out.println("Sistema de Delivery Fora do AR, Movimentacao interna");

        BigDecimal precoInterno = calculateTax(dto);

        return new DeliveryShipResponse(
                "INTERNAL", precoInterno, calcuteShippingTime(dto)
        );
    }


    private BigDecimal calculateTax(FreightRequestDTO dto) {
        BigDecimal precoPorKm = BigDecimal.valueOf(1.50);
        return precoPorKm.multiply(BigDecimal.valueOf(dto.distanceKm()));
    }

    private int calcuteShippingTime(FreightRequestDTO dto) {
        return dto.distanceKm().intValue() > 20 ? 120 : 60;
    }
}
