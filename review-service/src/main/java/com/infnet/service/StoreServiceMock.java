package com.infnet.service;

import com.infnet.api.dto.ValidacaoStoreResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class StoreServiceMock implements StoreService {
    @Override
    public ValidacaoStoreResponse validarStore(Long storeId) {
        return new ValidacaoStoreResponse(
                storeId,
                3L
        );
    }
}