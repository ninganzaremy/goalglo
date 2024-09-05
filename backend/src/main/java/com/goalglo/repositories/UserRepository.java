package com.goalglo.repositories;

import com.goalglo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
   Optional<User> findByEmail(String email);
   Optional<User> findByUsername(String username);

   Optional<User> findByEmailOrUsername(String email, String username);


}