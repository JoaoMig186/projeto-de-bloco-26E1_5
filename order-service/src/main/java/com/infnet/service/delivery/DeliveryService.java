package com.infnet.service.delivery;

import com.infnet.dto.delivery.DeliveryShipResponse;
import com.infnet.dto.delivery.FreightRequestDTO;

public interface DeliveryService {
    DeliveryShipResponse getDeliveryPrice(
            FreightRequestDTO dto
    );
}
