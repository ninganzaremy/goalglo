package com.goalglo.backend.controllers;

import com.goalglo.backend.entities.User;
import com.goalglo.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

   private final UserService userService;

   @Autowired
   public AuthController(UserService userService) {
      this.userService = userService;
   }

   @PostMapping("/register")
   public ResponseEntity<User> registerUser(@RequestBody User user) {
      User registeredUser = userService.registerUser(user);
      return ResponseEntity.ok(registeredUser);
   }

   @PostMapping("/login")
   public ResponseEntity<User> loginUser(@RequestBody User user) {
      return userService.loginUser(user.getEmail(), user.getPassword())
         .map(ResponseEntity::ok)
         .orElse(ResponseEntity.status(401).build());
   }
}