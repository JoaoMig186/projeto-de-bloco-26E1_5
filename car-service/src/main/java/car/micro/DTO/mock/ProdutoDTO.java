package car.micro.DTO.mock;

import java.math.BigDecimal;

public record ProdutoDTO(
        Long id,
        String nome,
        BigDecimal preco,
        Double peso,
        Boolean fragil
) {}