package com.infnet.service.user;

import com.infnet.dto.store.GeocodeResponseDTO;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserService {
    GeocodeResponseDTO getGeocode(Long id);
}
