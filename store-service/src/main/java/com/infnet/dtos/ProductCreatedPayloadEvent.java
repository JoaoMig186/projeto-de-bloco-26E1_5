package com.infnet.dtos;

import com.infnet.model.Product;
import com.infnet.model.Store;
import com.infnet.model.enums.Category;
import com.infnet.model.enums.Durability;

import java.math.BigDecimal;

public record ProductCreatedPayloadEvent(
        Store store,
        Long id,
        String name,
        String description,
        BigDecimal price,
        Category category,
        Durability durability

) {
    public static ProductCreatedPayloadEvent toProductCreatedPayloadEvent (Product product){
        return new ProductCreatedPayloadEvent(
                product.getStore(),
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getDurability()
        );
    }

}
