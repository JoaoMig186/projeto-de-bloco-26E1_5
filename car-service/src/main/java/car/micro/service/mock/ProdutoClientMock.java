package car.micro.service.mock;

import car.micro.DTO.mock.ProdutoDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ProdutoClientMock {

    private final Map<Long, ProdutoDTO> produtosMock = Map.of(
            1L, new ProdutoDTO(1L, "Notebook Gamer", new BigDecimal("4500.00"), 2.5, true),
            2L, new ProdutoDTO(2L, "Mouse Sem Fio", new BigDecimal("150.00"), 0.2, false),
            3L, new ProdutoDTO(3L, "Monitor Ultrawide", new BigDecimal("2500.00"), 6.0, true),
            4L, new ProdutoDTO(4L, "Teclado Mecânico", new BigDecimal("350.00"), 1.1, false)
    );

    public ProdutoDTO buscarProduto(Long produtoId) {
        return produtosMock.get(produtoId);
    }
}