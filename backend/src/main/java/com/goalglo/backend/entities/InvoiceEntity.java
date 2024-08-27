package com.goalglo.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "invoices")
public class InvoiceEntity {
   @Id
   @GeneratedValue
   private UUID id;

   @ManyToOne
   @JoinColumn(name = "payment_id", nullable = false)
   private Payment payment;

   @ManyToOne
   @JoinColumn(name = "user_id", nullable = false)
   private User user;

   @Column(nullable = false, unique = true)
   private String invoiceNumber;

   @Column(nullable = false)
   private LocalDate issueDate;

   @Column(nullable = false)
   private LocalDate dueDate;

   @Column(nullable = false)
   private BigDecimal amountDue;

   @Column(nullable = false)
   private String status;

   @CreationTimestamp
   private LocalDateTime createdAt;

   @UpdateTimestamp
   private LocalDateTime updatedAt;
}