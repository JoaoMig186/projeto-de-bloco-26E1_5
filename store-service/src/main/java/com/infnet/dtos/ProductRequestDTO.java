package com.infnet.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message = "O nome do produto é obrigatório")
        String name,

        String description,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
        BigDecimal price,

        @NotNull(message = "A quantidade em stock é obrigatória")
        @Min(value = 0, message = "O stock não pode ser negativo")
        Integer stockQuantity,

        @NotBlank(message = "A categoria é obrigatória (ex: pisos, cimentos)")
        String category,

        @NotNull(message = "O ID da loja é obrigatório")
        Long storeId
) {}