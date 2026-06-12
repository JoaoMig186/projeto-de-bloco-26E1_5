package car.micro.DTO;

import java.math.BigDecimal;

public record CheckoutIniciadoEvent(
        Long carrinhoId,
        Long usuarioId,
        BigDecimal total
) {
}