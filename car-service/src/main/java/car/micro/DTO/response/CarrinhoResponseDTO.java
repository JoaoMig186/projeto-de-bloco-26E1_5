package car.micro.DTO.response;

import java.math.BigDecimal;
import java.util.List;

public record CarrinhoResponseDTO(

        Long carrinhoId,

        Long usuarioId,

        List<ItemCarrinhoResponseDTO> itens,

        BigDecimal total

) {
}