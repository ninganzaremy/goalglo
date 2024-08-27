package com.goalglo.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {
   @Id
   @GeneratedValue
   private UUID id;

   @ManyToOne
   @JoinColumn(name = "user_id", nullable = false)
   private User user;

   @ManyToOne
   @JoinColumn(name = "service_id", nullable = false)
   private ServiceEntity service;

   @Column(nullable = false)
   private Long amount;

   @Column(nullable = false)
   private String currency;

   @Column(nullable = false)
   private String status;

   @Column(nullable = false)
   private String paymentMethod;

   @Column(nullable = false, unique = true)
   private String stripePaymentId;

   @CreationTimestamp
   private LocalDateTime createdAt;

   @UpdateTimestamp
   private LocalDateTime updatedAt;
}