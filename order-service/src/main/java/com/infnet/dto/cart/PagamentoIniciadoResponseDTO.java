package com.infnet.dto.cart;

import java.math.BigDecimal;
import java.util.List;

public record PagamentoIniciadoResponseDTO(
        Long carrinhoId,
        BigDecimal valorTotal,
        BigDecimal valorTotalKg,
        List<ItemCarrinhoResponseDTO> itens
) {
}
