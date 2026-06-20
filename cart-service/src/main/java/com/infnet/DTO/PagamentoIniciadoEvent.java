package com.infnet.DTO;

import com.infnet.DTO.response.ItemCarrinhoResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public record PagamentoIniciadoEvent(
        Long carrinhoId,
        BigDecimal valorTotal,
        BigDecimal valorTotalKg,
        List<ItemCarrinhoResponseDTO> itens
) {}