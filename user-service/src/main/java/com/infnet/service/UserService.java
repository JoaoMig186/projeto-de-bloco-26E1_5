package com.infnet.service;

import com.infnet.dtos.UserLoginDTO;
import com.infnet.dtos.UserRegisterDTO;
import com.infnet.exception.AuthorizationException;
import com.infnet.exception.EmailAlreadyRegisteredException;
import com.infnet.model.User;
import com.infnet.model.enums.Role;
import com.infnet.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository repository;
    private PasswordEncoder encoder;
    private JwtService jwtService;

    public String userAuthLogin(UserLoginDTO dto) {
        User user = repository.findUserByEmail(dto.email()).orElseThrow(AuthorizationException::new);

        if(!encoder.matches(dto.password(),user.getPassword())){
            throw new AuthorizationException();
        }

        return jwtService.generateToken(user);
    }

    public void userTokenValidation(String token){
        Claims claims = jwtService.validateToken(token);
        System.out.println("SLA MN: " + claims );
    }

    public User registerUser(UserRegisterDTO dto, Role role) {
        repository.findUserByEmail(dto.email()).ifPresent(user -> {
            throw new EmailAlreadyRegisteredException(("Email already registered"));
        });

        String passwordHash = encoder.encode(dto.password());

        User user = dto.toCustomerDomain(dto,role);
        user.setPassword(passwordHash);

        return repository.save(user);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

}
