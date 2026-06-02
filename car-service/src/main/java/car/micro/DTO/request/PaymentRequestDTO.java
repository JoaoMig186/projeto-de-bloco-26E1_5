package car.micro.DTO.request;

import java.math.BigDecimal;

public record PaymentRequestDTO(

        Long carrinhoId,

        Long usuarioId,

        BigDecimal valorTotal

) {
}
