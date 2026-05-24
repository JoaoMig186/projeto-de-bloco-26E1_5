package com.infnet.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record StoreRequestDTO(
        @NotBlank(message = "O nome da loja é obrigatório")
        String name,

        @NotBlank(message = "O CNPJ é obrigatório")
        @Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter exatamente 14 dígitos numéricos")
        String cnpj,

        @NotBlank(message = "O endereço é obrigatório")
        String address,

        @NotNull(message = "A latitude é obrigatória para a busca geolocalizada")
        Double latitude,

        @NotNull(message = "A longitude é obrigatória para a busca geolocalizada")
        Double longitude,

        String phone
) {}