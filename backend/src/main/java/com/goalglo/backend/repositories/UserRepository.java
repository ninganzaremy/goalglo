package com.goalglo.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.goalglo.backend.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
   User findByUsernameAndPassword(String username, String password);
}