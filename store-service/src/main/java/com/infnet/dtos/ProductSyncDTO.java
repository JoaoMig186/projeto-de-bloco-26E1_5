package com.infnet.dtos;

import java.math.BigDecimal;

public record ProductSyncDTO(
        Long id,
        String name,
        com.infnet.model.enums.Category category,
        BigDecimal price,
        // Informações da loja embutidas para o Elastic Search não ter que fazer JOIN
        Long storeId,
        String storeName,
        Double storeLatitude,
        Double storeLongitude
) {}