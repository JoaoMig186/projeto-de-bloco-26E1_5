package car.micro.service.mock;

import car.micro.DTO.mock.LojaDTO;
import org.springframework.stereotype.Service;

@Service
public class LojaClientMock {

    public LojaDTO buscarLoja(Long lojaId) {

        return new LojaDTO(
                lojaId,
                "Loja Tech",
                "24020-125"
        );
    }
}
