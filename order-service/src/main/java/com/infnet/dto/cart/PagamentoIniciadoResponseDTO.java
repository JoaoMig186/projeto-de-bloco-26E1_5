package com.infnet.dto.cart;

import java.math.BigDecimal;
import java.util.List;

public record PagamentoIniciadoResponseDTO(
        Long carrinhoId,
        Long usuarioId,
        BigDecimal valorTotal,
        List<ItemCarrinhoResponseDTO> itens,
        BigDecimal pesoTotalCart
) {
}
