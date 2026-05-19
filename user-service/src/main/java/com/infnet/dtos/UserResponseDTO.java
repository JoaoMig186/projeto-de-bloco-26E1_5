package com.infnet.dtos;

import com.infnet.model.User;
import com.infnet.model.enums.Role;

public record UserResponseDTO(
        String name,
        String email,
        String password,
        Role role
) {

    public static UserResponseDTO toResponseDTO(User user){
        return new UserResponseDTO(
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()
        );
    }

}
