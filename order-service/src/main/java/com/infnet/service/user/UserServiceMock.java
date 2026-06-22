package com.infnet.service.user;

import com.infnet.dto.store.GeocodeResponseDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"dev"})
public class UserServiceMock implements UserService {
    @Override
    public GeocodeResponseDTO getGeocode(Long id) {
        return new GeocodeResponseDTO(
                -89.1729, -2.9068
        );
    }
}
