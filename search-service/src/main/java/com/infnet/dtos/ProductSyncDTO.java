package com.infnet.dtos;


import java.math.BigDecimal;

public record ProductSyncDTO(
        Long id,
        String name,
        String description,
        String category,
        String durability,
        BigDecimal price,
        Long storeId,
        String storeName
){

}