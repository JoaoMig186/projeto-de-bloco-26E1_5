package car.micro.service.mock;

import car.micro.DTO.mock.LojaDTO;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LojaClientMock {

    private final Map<Long, LojaDTO> lojasMock = Map.of(
            1L, new LojaDTO(1L, "Loja Tech - Matriz (SP)", "01001-000"),
            2L, new LojaDTO(2L, "Loja Tech - Filial (RJ)", "24020-125")
    );

    public LojaDTO buscarLoja(Long lojaId) {
        return lojasMock.get(lojaId);
    }
}