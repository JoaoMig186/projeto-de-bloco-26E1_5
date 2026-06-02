package car.micro.DTO.response;

import java.math.BigDecimal;

public record ItemCarrinhoResponseDTO(

        Long itemId,

        String nomeProduto,

        Integer quantidade,

        BigDecimal preco,

        BigDecimal subtotal

) {
}