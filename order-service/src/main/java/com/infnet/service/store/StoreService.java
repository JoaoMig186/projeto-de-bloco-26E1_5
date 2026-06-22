package com.infnet.service.store;

import com.infnet.dto.store.GeocodeResponseDTO;
import org.springframework.web.bind.annotation.PathVariable;

public interface StoreService {
    GeocodeResponseDTO getGeocode( Long id);
}
