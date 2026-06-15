package car.micro.service.mock;

import car.micro.DTO.mock.UsuarioDTO;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UsuarioClientMock {

    private final Map<Long, UsuarioDTO> usuariosMock = Map.of(
            1L, new UsuarioDTO(1L),
            2L, new UsuarioDTO(2L),
            3L, new UsuarioDTO(3L)
    );

    public UsuarioDTO buscarUsuario(Long id) {
        return usuariosMock.get(id);
    }
}