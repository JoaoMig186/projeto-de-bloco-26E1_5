package car.micro.DTO.response;

import java.math.BigDecimal;
public record ItemCarrinhoResponseDTO(
        Long itemId,
        Long lojaId,
        String nomeProduto,
        Integer quantidade,
        Double peso,
        Boolean fragil,
        String cepLoja
) {}