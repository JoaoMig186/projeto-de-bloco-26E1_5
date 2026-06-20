package com.infnet.DTO;

public record CartItemResponseDTO(
        Long itemId,
        Long storeId,
        String productName,
        Integer quantity,
        Double weight,
        Boolean fragile,
        StoreDTO store
) {}