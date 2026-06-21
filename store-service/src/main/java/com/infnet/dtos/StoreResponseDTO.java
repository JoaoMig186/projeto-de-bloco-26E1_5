package com.infnet.dtos;

import com.infnet.model.Store;

public record StoreResponseDTO(
        Long id,
        String name,
        String cnpj,
        String address,
        Double latitude,

        Double longitude,
        String phone,
        boolean active
) {
    // Construtor prático para converter diretamente da Entidade para o DTO
    public StoreResponseDTO(Store store) {
        this(
                store.getId(),
                store.getName(),
                store.getCnpj(),
                store.getAddress(),
                store.getLatitude(),
                store.getLongitude(),
                store.getPhone(),
                store.isActive()
        );
    }
}