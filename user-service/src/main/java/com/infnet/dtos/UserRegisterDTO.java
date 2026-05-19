package com.infnet.dtos;

import com.infnet.model.User;
import com.infnet.model.enums.Role;

public record UserRegisterDTO(
        String name,
        String email,
        String password
) {

    public User toCustomerDomain(UserRegisterDTO dto,Role role){
        return new User(
                dto.name(),
                dto.email(),
                dto.password(),
                role
        );
    }

}
