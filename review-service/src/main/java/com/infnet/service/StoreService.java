package com.infnet.service;

import com.infnet.api.dto.ValidacaoStoreResponse;
import com.infnet.api.exception.StoreNotFoundException;
import com.infnet.api.exception.StoreUnavailableException;
import com.infnet.client.StoreClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreClient client;

    @CircuitBreaker(name = "storeValidation", fallbackMethod = "storeUnavailableFallback")
    public ValidacaoStoreResponse validarStore(Long storeId) {
        return client.validarStore(storeId);
    }

    public ValidacaoStoreResponse storeUnavailableFallback(Long storeId, Throwable tw) {
        // store-service retorna 400 (não 404) para loja inexistente — tratamos BadRequest como NotFound.
        // Remover quando store-service corrigir para devolver 404.
        if (tw instanceof feign.FeignException.NotFound || tw instanceof feign.FeignException.BadRequest) {
            throw new StoreNotFoundException("Loja não encontrada");
        }

        throw new StoreUnavailableException("Não foi possível validar a loja");
    }
}
