package com.infnet.dtos;

import com.infnet.model.Product;
import com.infnet.model.enums.Category;
import com.infnet.model.enums.Durability;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        Category category,
        Durability durability,
        Long storeId,
        String storeName
) {
    public ProductResponseDTO(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory(),
                product.getDurability(),
                product.getStore().getId(),
                product.getStore().getName()
        );
    }
}