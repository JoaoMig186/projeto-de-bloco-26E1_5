package com.infnet.DTO;

import java.math.BigDecimal;

public record ProductOrderInfoDTO(
        Long itemId,
        Long storeId,
        String productName,
        BigDecimal preco,
        Double weight,
        Boolean fragile,
        StoreDTO store
) {}