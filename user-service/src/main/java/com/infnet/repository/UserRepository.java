package com.infnet.repository;

import com.infnet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findUserByEmailIgnoreCase(String email);
    
}
