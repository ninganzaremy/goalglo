package com.goalglo.backend.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class Client {
   @Id
   @GeneratedValue
   private UUID id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id", nullable = false)
   private User user;

   @Column(nullable = false)
   private String firstName;

   @Column(nullable = false)
   private String lastName;

   @Column(nullable = false)
   private String email;

   @Column(nullable = false)
   private String phoneNumber;

   @Column
   private String address;

   @Column(name = "created_at", nullable = false, updatable = false)
   private LocalDateTime createdAt = LocalDateTime.now();

   @Column(name = "updated_at", nullable = false)
   private LocalDateTime updatedAt = LocalDateTime.now();

   @PreUpdate
   protected void onUpdate() {
      updatedAt = LocalDateTime.now();
   }
}