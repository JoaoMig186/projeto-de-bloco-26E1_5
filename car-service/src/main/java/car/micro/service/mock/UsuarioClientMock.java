package car.micro.service.mock;

import car.micro.DTO.mock.UsuarioDTO;
import org.springframework.stereotype.Service;

@Service
public class UsuarioClientMock {

    public UsuarioDTO buscarUsuario(Long id) {
        return new UsuarioDTO(
                id,
                "Lucas Gomes",
                "lucas@email.com"
        );
    }
}