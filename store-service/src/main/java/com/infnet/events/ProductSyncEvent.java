package com.infnet.events;

import java.math.BigDecimal;

public record ProductSyncEvent(
        Long id,
        String name,
        String category,
        BigDecimal price,
        Long storeId,
        String storeName,
        Double storeLatitude,
        Double storeLongitude
) {}