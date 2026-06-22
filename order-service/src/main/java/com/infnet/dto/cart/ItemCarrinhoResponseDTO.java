package com.infnet.dto.cart;

public record ItemCarrinhoResponseDTO(
        Long itemId,
        Long lojaId,
        String nomeProduto,
        Integer quantidade,
        Double peso,
        Boolean fragil
) {
}
