package com.infnet.service.user;

import com.infnet.client.UserIClient;
import com.infnet.dto.store.GeocodeResponseDTO;
import com.infnet.excepton.GeocodeServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile({"dev"})
public class UserServiceImpl implements UserService {
    private final UserIClient client;

    @Override
    @Retry(name = "deliveryService")
    @CircuitBreaker(name = "deliveryService", fallbackMethod = "fallBackGeocode")
    public GeocodeResponseDTO getGeocode(Long id) {
        return client.getGeocode(id);
    }
    private GeocodeResponseDTO fallBackGeocode(Long id, Throwable throwable) {
        throw new GeocodeServiceUnavailableException(
                "Não foi possível obter a localização do usuário. Tente novamente em instantes.");
    }

}
