package com.goalglo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.goalglo.backend.entities.User;
import com.goalglo.backend.repositories.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

   private final UserRepository userRepository;

   @Autowired
   public AuthController(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @PostMapping("/register")
   public User registerUser(@RequestBody User user) {
      return userRepository.save(user);
   }

   @PostMapping("/login")
   public User loginUser(@RequestBody User user) {
      return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
   }
}