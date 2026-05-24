package com.infnet.dtos;

import com.infnet.model.Store;

public record StoreResponseDTO(
        Long id,
        String name,
        String cnpj,
        String address,
        String phone,
        double longitude,
        double latitude,
        boolean active
) {
    // Construtor prático para converter diretamente da Entidade para o DTO
    public StoreResponseDTO(Store store) {
        this(
                store.getId(),
                store.getName(),
                store.getCnpj(),
                store.getAddress(),
                store.getPhone(),
                store.getLongitude(),
                store.getLatitude(),
                store.isActive()
        );
    }
}