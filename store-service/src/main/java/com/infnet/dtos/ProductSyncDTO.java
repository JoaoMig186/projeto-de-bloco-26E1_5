package com.infnet.dtos;

import com.infnet.model.Product;
import com.infnet.model.enums.Category;
import com.infnet.model.enums.Durability;

import java.math.BigDecimal;

public record ProductSyncDTO(
        Long id,
        String name,
        String description,
        Category category,
        Durability durability,
        BigDecimal price,
        Long storeId,
        String storeName
) {
    public static ProductSyncDTO fromDomain(Product product){
        return new ProductSyncDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getDurability(),
                product.getPrice(),
                product.getStore().getId(),
                product.getStore().getName()
        );
    }
}