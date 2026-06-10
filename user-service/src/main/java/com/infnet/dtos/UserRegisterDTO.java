package com.infnet.dtos;

import com.infnet.model.User;
import com.infnet.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.aspectj.weaver.ast.Not;
import org.hibernate.validator.constraints.Length;

public record UserRegisterDTO(
        @NotBlank
        String name,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Length(min = 5,max = 64)
        String password,
        String address
) {

    public User toUserDomain(UserRegisterDTO dto,Role role){
        return new User(
                dto.name(),
                dto.email(),
                dto.password(),
                role
        );
    }

}
