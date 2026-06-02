package car.micro.service.mock;

import car.micro.DTO.mock.ProdutoDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProdutoClientMock {

    public ProdutoDTO buscarProduto(Long produtoId) {

        return new ProdutoDTO(
                produtoId,
                "Notebook Gamer",
                new BigDecimal("4500.00"),
                2.5,
                true
        );
    }
}
