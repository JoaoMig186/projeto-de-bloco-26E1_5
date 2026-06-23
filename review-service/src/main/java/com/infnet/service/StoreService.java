package com.infnet.service;

import com.infnet.api.dto.ValidacaoStoreResponse;

public interface StoreService {
    ValidacaoStoreResponse validarStore(Long storeId);
}