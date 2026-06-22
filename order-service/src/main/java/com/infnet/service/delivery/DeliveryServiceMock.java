package com.infnet.service.delivery;

import com.infnet.dto.delivery.DeliveryShipResponse;
import com.infnet.dto.delivery.FreightRequestDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Profile({"dev"})
public class DeliveryServiceMock implements DeliveryService {
    @Override
    public DeliveryShipResponse getDeliveryPrice(FreightRequestDTO dto) {
        return new DeliveryShipResponse(
                "Car",
                BigDecimal.TEN,
                10
        );
    }
}
