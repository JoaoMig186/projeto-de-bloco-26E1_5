package car.micro.DTO.response;

import java.math.BigDecimal;

public record DeliveryResponseDTO(

        BigDecimal frete,

        Integer prazoDias

) {
}
