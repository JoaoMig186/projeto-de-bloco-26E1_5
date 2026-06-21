package com.infnet.client;

import com.infnet.dto.store.GeocodeResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${api.endpoints.store}")
public interface StoreIClient {
    @GetMapping("/stores/{id}/geocode")
    public GeocodeResponseDTO getGeocode(@PathVariable Long id);
}
