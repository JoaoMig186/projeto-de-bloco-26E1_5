package com.infnet.dtos;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.infnet.model.ProductDocument;
import com.infnet.model.enums.Category;

import java.math.BigDecimal;

public record ProductResponseDTO(
        String id,
        String name,
        String description,
        String category,
        String durability,
        BigDecimal price,
        String storeName,
        Double score
) {
    public static ProductResponseDTO toDTO(Hit<ProductDocument> hit) {
        ProductDocument doc = hit.source();
        return new ProductResponseDTO(
                hit.id(),
                doc.getName(),
                doc.getDescription(),
                doc.getCategory(),
                doc.getDurability(),
                doc.getPrice(),
                doc.getStoreName(),
                hit.score()
        );
    }
}
