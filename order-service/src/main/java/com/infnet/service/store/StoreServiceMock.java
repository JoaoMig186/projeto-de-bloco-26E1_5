package com.infnet.service.store;

import com.infnet.dto.store.GeocodeResponseDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"dev"})
public class StoreServiceMock implements StoreService {
    @Override
    public GeocodeResponseDTO getGeocode(Long id) {
        return new GeocodeResponseDTO(
                -43.1729, -22.9068
        );
    }
}
