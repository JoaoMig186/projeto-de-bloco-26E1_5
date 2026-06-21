package com.infnet.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateReplyRequest(
        @NotBlank(message = "Resposta não pode ser vazia")
        @Size(max = 500, message = "Resposta deve ter no máximo 500 caracteres")
        String comment) {
}