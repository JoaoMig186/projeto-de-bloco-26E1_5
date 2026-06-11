package com.infnet.client;

import com.infnet.api.dto.ValidacaoStoreResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${api.endpoints.store}")
public interface StoreClient {
    @GetMapping("/stores/{id}")
    ValidacaoStoreResponse validarStore(@PathVariable("id") Long id);
}