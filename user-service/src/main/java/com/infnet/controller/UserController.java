package com.infnet.controller;

import com.infnet.dtos.*;
import com.infnet.model.CustomerProfile;
import com.infnet.model.User;
import com.infnet.model.enums.Role;
import com.infnet.service.UserService;
import jakarta.validation.Valid;
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

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("userId") Long id){
        User user = service.getUserById(authHeader, id);
        return ResponseEntity.ok().body(UserResponseDTO.toResponseDTO(user));

    }

    @PostMapping("/customer")
    public ResponseEntity<UserResponseDTO> registerCustomer(@RequestBody @Valid UserRegisterDTO dto){
        User user = service.registerUser(dto, Role.CUSTOMER);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.toResponseDTO(user));
    }

    @PostMapping("/store")
    public ResponseEntity<UserResponseDTO> registerStoreOwner(@RequestBody @Valid UserRegisterDTO dto){
        User user = service.registerUser(dto, Role.STORE_OWNER);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.toResponseDTO(user));
    }

    @PostMapping("/admin")
    public ResponseEntity<UserResponseDTO> registerAdmin(@RequestBody @Valid UserRegisterDTO dto){
        User user = service.registerUser(dto, Role.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.toResponseDTO(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(
            @RequestHeader("Authorization") String authHeader
    ){
        service.adminValidation(authHeader);
        List<User> userList = service.getAllUsers();
        return ResponseEntity.ok().body(userList.stream().map(UserResponseDTO::toResponseDTO).toList());
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers(
        @RequestHeader("Authorization") String authHeader
    ){
        service.adminValidation(authHeader);
        List<CustomerProfile> customersList = service.getAllCustomers();
        return ResponseEntity.ok().body(customersList.stream().map(CustomerResponseDTO::toCustomerResponseDTO).toList());
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("customerId") Long id){
        CustomerProfile customer = service.getCustomerById(authHeader, id);
        return ResponseEntity.ok().body(CustomerResponseDTO.toCustomerResponseDTO(customer));

    }


}
