package com.infnet.controller;

import com.infnet.dtos.UserLoginDTO;
import com.infnet.dtos.UserRegisterDTO;
import com.infnet.dtos.UserResponseDTO;
import com.infnet.model.User;
import com.infnet.model.enums.Role;
import com.infnet.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService service;

    @PostMapping("/login")
    public String userAuthLogin(@RequestBody UserLoginDTO dto){
        return service.userAuthLogin(dto);
    }

    @PostMapping("/validation")
    public void userTokenValidation(@RequestHeader("Authorization") String authHeader){
        service.userTokenValidation(authHeader.split(" ")[1]);
    }

    @PostMapping("/customer")
    public ResponseEntity<UserResponseDTO> registerCustomer(@RequestBody UserRegisterDTO dto){
        User user = service.registerUser(dto, Role.CUSTOMER);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.toResponseDTO(user));
    }

    @PostMapping("/store")
    public ResponseEntity<UserResponseDTO> registerStoreOwner(@RequestBody UserRegisterDTO dto){
        User user = service.registerUser(dto, Role.STORE_OWNER);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.toResponseDTO(user));
    }

    @PostMapping("/admin")
    public ResponseEntity<UserResponseDTO> registerAdmin(@RequestBody UserRegisterDTO dto){
        User user = service.registerUser(dto, Role.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.toResponseDTO(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        List<User> userList = service.getAllUsers();
        return ResponseEntity.ok().body(userList.stream().map(UserResponseDTO::toResponseDTO).toList());
    }

}
