package com.infnet.service.store;

import com.infnet.client.StoreIClient;
import com.infnet.dto.store.GeocodeResponseDTO;
import com.infnet.excepton.GeocodeServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile({"prod"})
public class StoreServiceImpl implements StoreService {
    private final StoreIClient client;

    @Override
    @CircuitBreaker(name = "storeService", fallbackMethod = "fallBackGeocode")
    @Retry(name = "storeService")
    public GeocodeResponseDTO getGeocode(Long id) {
        return client.getGeocode(id);
    }
    private GeocodeResponseDTO fallBackGeocode(Long id, Throwable throwable) {
        throw new GeocodeServiceUnavailableException(
                "Não foi possível obter a localização da Loja. Tente novamente em instantes.");
    }

}
