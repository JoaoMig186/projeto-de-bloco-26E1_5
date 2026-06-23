package com.infnet.dto.cart;

public record ItemCarrinhoResponseDTO(
        Long itemId,
        Long storeId,
        String productName,
        Double weight,
        Boolean fragile,
        Integer quantity
) {
}
