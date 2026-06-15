package car.micro.DTO;

import car.micro.DTO.response.ItemCarrinhoResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public record PagamentoIniciadoEvent(
        Long carrinhoId,
        Long usuarioId,
        BigDecimal valorTotal,
        List<ItemCarrinhoResponseDTO> itens
) {}