package com.infnet.service.cart;

import com.infnet.dto.cart.PagamentoIniciadoResponseDTO;

public interface CartService {
    PagamentoIniciadoResponseDTO getCart(Long usuarioId);
}
