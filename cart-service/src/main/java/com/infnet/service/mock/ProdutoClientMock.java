package com.infnet.service.mock;

import com.infnet.DTO.StoreDTO;
import com.infnet.DTO.ProductOrderInfoDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ProdutoClientMock {

    private final Map<Long, ProductOrderInfoDTO> produtosMock = Map.of(
            1L, new ProductOrderInfoDTO(
                    1L,
                    1L,
                    "Notebook Gamer",
                    new BigDecimal("4500.00"),
                    2.5,
                    true,
                    new StoreDTO(1L, "Loja Tech - Matriz (SP)", -23.550520, -46.633308)
            ),
            2L, new ProductOrderInfoDTO(
                    2L,
                    1L,
                    "Mouse Sem Fio",
                    new BigDecimal("150.00"),
                    0.2,
                    false,
                    new StoreDTO(2L, "Loja Tech - Filial (RJ)", -22.906847, -43.172896)
            )
    );

    public ProductOrderInfoDTO buscarProduto(Long produtoId) {
        return produtosMock.get(produtoId);
    }
}