package com.infnet.DTO;

import java.math.BigDecimal;
import java.util.List;

public record PagamentoIniciadoEvent(
        Long carrinhoId,
        BigDecimal valorTotal,
        BigDecimal valorTotalKg,
        List<CartItemResponseDTO> itens
) {}