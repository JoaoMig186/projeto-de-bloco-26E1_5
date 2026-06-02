package car.micro.DTO.request;

import car.micro.DTO.ItemDeliveryDTO;

import java.math.BigDecimal;
import java.util.List;

public record DeliveryRequestDTO(

        Long carrinhoId,

        Long usuarioId,

        BigDecimal total,

        String cepEntrega,

        List<ItemDeliveryDTO> itens

) {
}