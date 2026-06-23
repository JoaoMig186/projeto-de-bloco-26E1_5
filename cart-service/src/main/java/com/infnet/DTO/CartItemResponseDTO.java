package com.infnet.DTO;

import java.math.BigDecimal;

public record CartItemResponseDTO(
        Long itemId,
        Long storeId,
        String productName,
        BigDecimal price,
        Double weight,
        Boolean fragile,
        Integer quantity,
        StoreDTO store
) {}