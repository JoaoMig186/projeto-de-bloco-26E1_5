package com.infnet.client;


import com.infnet.dto.delivery.DeliveryShipResponse;
import com.infnet.dto.delivery.FreightRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${api.endpoints.delivery}")
public interface DeliveryIClient {
    @GetMapping("freight/calculate")
    DeliveryShipResponse getDeliveryPrice(
            @RequestBody FreightRequestDTO dto
    );
}
