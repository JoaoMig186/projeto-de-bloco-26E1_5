package car.micro.service.mock;

import car.micro.DTO.mock.LojaDTO;
import car.micro.DTO.mock.ProdutoDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ProdutoClientMock {

    private final Map<Long, ProdutoDTO> produtosMock = Map.of(
            1L, new ProdutoDTO(
                    1L,
                    "Notebook Gamer",
                    new BigDecimal("4500.00"),
                    2.5,
                    true,
                    new LojaDTO(1L, "Loja Tech - Matriz (SP)", -23.550520, -46.633308)
            ),
            2L, new ProdutoDTO(
                    2L,
                    "Mouse Sem Fio",
                    new BigDecimal("150.00"),
                    0.2,
                    false,
                    new LojaDTO(2L, "Loja Tech - Filial (RJ)", -22.906847, -43.172896)
            )
    );

    public ProdutoDTO buscarProduto(Long produtoId) {
        return produtosMock.get(produtoId);
    }
}