package com.infnet.DTO;

import java.math.BigDecimal;
import java.util.List;

public record CartResponseDTO(

        Long cartId,

        Long userId,

        List<CartItemResponseDTO> items,

        BigDecimal total

) {
}