package com.infnet.service.cart;

import com.infnet.dto.cart.PagamentoIniciadoResponseDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Profile({"dev"})
public class CartServiceMock implements CartService{
    @Override
    public PagamentoIniciadoResponseDTO getCart(Long usuarioId) {
        return new PagamentoIniciadoResponseDTO(
                1L, 2L, BigDecimal.TEN, null, BigDecimal.TEN
        );
    }
}
