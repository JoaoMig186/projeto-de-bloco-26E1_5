package com.infnet.dtos;

import java.util.List;

public record StoreProductsDTO(
        Long storeId,
        String storeName,
        List<ProductResponseDTO> products
) {
}