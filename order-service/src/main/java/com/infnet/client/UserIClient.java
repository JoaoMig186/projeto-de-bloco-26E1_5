package com.infnet.client;

import com.infnet.dto.store.GeocodeResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${api.endpoints.user}")
public interface UserIClient {
    @GetMapping("/users/{id}/geocode")
    GeocodeResponseDTO getGeocode(@PathVariable("id") Long id);
}
