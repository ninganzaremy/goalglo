package com.goalglo.backend.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "email_verification_tokens")
@Getter
@Setter
@NoArgsConstructor
public class EmailVerificationToken {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private UUID id;

   @Column(nullable = false, unique = true)
   private String token;

   @Column(nullable = false)
   private LocalDateTime expirationDate;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id", nullable = false)
   private User user;

   public EmailVerificationToken(User user) {
      this.user = user;
      this.token = UUID.randomUUID().toString();
      this.expirationDate = LocalDateTime.now().plusHours(24); // Token valid for 24 hours
   }
}