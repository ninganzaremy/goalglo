package com.goalglo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
   @Id
   @GeneratedValue
   private UUID id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id", nullable = false)
   @ToString.Exclude
   private User user;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "payment_id")
   @ToString.Exclude
   private Payment payment;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "service_id")
   @ToString.Exclude
   private ServiceEntity service;

   @Column(nullable = false)
   private BigDecimal amount;

   @Column(nullable = false)
   private String type;

   private String description;

   @Column(name = "transaction_date", nullable = false)
   private LocalDateTime transactionDate;

   @Column(name = "created_at")
   private LocalDateTime createdAt;

   @Column(name = "updated_at")
   private LocalDateTime updatedAt;

   @PrePersist
   protected void onCreate() {
      createdAt = LocalDateTime.now();
      updatedAt = LocalDateTime.now();
      if (transactionDate == null) {
         transactionDate = LocalDateTime.now();
      }
   }

   @PreUpdate
   protected void onUpdate() {
      updatedAt = LocalDateTime.now();
   }

   @Override
   public final boolean equals(Object o) {
      if (this == o) return true;
      if (o == null) return false;
      Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
      Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
      if (thisEffectiveClass != oEffectiveClass) return false;
      Transaction that = (Transaction) o;
      return getId() != null && Objects.equals(getId(), that.getId());
   }

   @Override
   public final int hashCode() {
      return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
   }
}