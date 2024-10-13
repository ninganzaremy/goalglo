package com.goalglo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "contact_message")
public class ContactMessage {
   @Id
   @GeneratedValue
   private UUID id;

   @Column(nullable = false)
   private String name;

   @Column(nullable = false)
   private String email;

   @Column(nullable = false)
   private String subject;

   @Column(nullable = false)
   private String message;

   @CreationTimestamp
   private LocalDateTime createdAt;
}