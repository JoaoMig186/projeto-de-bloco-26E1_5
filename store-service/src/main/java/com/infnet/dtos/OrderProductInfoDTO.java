package com.infnet.dtos;

public record OrderProductInfoDTO(
        Long itemId,
        Long storeId,
        String productName,
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