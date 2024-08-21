package com.goalglo.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
   @Id
   @GeneratedValue
   private UUID id;

   @Column(nullable = false, unique = true)
   private String email;

   @Column(nullable = false, unique = true)
   private String username;

   @Column(nullable = false)
   private String password;

   @Column(nullable = false)
   private String role;

   @Column(nullable = false, columnDefinition = "boolean default false")
   private boolean emailVerified = false;

   @Column(name = "created_at", nullable = false, updatable = false)
   private LocalDateTime createdAt = LocalDateTime.now();

   @Column(name = "updated_at", nullable = false)
   private LocalDateTime updatedAt = LocalDateTime.now();

   @PreUpdate
   protected void onUpdate() {
      updatedAt = LocalDateTime.now();
   }
}