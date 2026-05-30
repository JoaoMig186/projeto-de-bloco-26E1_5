package com.infnet.dtos;

import com.infnet.model.User;
import com.infnet.model.enums.Role;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String password,
        Role role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static UserResponseDTO toResponseDTO(User user){
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

}
