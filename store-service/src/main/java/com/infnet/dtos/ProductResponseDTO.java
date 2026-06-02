package com.infnet.dtos;

import com.infnet.model.Product;
import com.infnet.model.enums.Category;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        Category category,
        Long storeId,
        String storeName
) {
    // Construtor prático para converter da Entidade para o DTO
    public ProductResponseDTO(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory(),
                product.getStore().getId(),
                product.getStore().getName()
        );
    }
}