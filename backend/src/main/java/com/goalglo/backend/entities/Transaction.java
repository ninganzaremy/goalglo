package com.goalglo.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {
   @Id
   @GeneratedValue
   private UUID id;

   @ManyToOne
   @JoinColumn(name = "payment_id", nullable = false)
   private Payment payment;

   @ManyToOne
   @JoinColumn(name = "service_id", nullable = false)
   private ServiceEntity service;

   @Column(nullable = false)
   private BigDecimal amount;

   @CreationTimestamp
   private LocalDateTime createdAt;

   @UpdateTimestamp
   private LocalDateTime updatedAt;
}