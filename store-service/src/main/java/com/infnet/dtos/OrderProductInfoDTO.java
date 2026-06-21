package com.infnet.dtos;

import java.math.BigDecimal;

public record OrderProductInfoDTO(
        Long itemId,
        Long storeId,
        String productName,
        BigDecimal price,
        Double weight,
        Boolean fragile,
        StoreDTO store
) {
    public record StoreDTO(
            Long id,
            String name,
            Double latitude,
            Double longitude
    ) {}
}