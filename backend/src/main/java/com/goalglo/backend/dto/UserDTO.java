package com.goalglo.backend.dto;

import com.goalglo.backend.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

   private UUID id;
   private String email;
   private String password;
   private String username;
   private String role;
   private String token;
   private boolean emailVerified = false;

   // Constructor to create UserDTO from User entity
   public UserDTO(User user) {
      this.id = user.getId();
      this.username = user.getUsername();
      this.email = user.getEmail();
      this.role = user.getRole();
      this.emailVerified = user.isEmailVerified();
      this.password = user.getPassword();
   }

   public String getIdentifier() {
      return this.email != null && !this.email.isEmpty() ? this.email : this.username;
   }

}