package com.infnet.service.mock;

import com.infnet.DTO.UserDTO;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UsuarioClientMock {

    private final Map<Long, UserDTO> usuariosMock = Map.of(
            1L, new UserDTO(1L),
            2L, new UserDTO(2L),
            3L, new UserDTO(3L)
    );

    public UserDTO buscarUsuario(Long id) {
        return usuariosMock.get(id);
    }
}