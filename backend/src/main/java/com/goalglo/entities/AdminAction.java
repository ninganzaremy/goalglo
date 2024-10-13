package com.goalglo.entities;

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
@Table(name = "admin_actions")
public class AdminAction {

   @Id
   @GeneratedValue
   private UUID id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "admin_id", nullable = false)
   private User admin;

   @Column(nullable = false)
   private String actionType;

   @Column(nullable = false)
   private String actionDetails;

   @Column(name = "created_at", nullable = false, updatable = false)
   private LocalDateTime createdAt = LocalDateTime.now();

   @PrePersist
   protected void onCreate() {
      createdAt = LocalDateTime.now();
   }
}