package com.goalglo.entities;

import com.goalglo.dto.PolicyDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "policies")
public class Policy {
   @Id
   @GeneratedValue
   private UUID id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id", nullable = false)
   private User user;

   @Column(nullable = false, unique = true)
   private String policyNumber;

   @Column(nullable = false)
   private String policyType;

   @Column(nullable = false)
   private double premiumAmount;

   @Column(nullable = false)
   private double coverageAmount;

   @Column(nullable = false)
   private LocalDate startDate;

   @Column
   private LocalDate endDate;

   @Column(name = "created_at", nullable = false, updatable = false)
   private LocalDateTime createdAt = LocalDateTime.now();

   @Column(name = "updated_at", nullable = false)
   private LocalDateTime updatedAt = LocalDateTime.now();

   @PreUpdate
   protected void onUpdate() {
      updatedAt = LocalDateTime.now();
   }
   public Policy(PolicyDTO policyDTO) {
      this.policyNumber = policyDTO.getPolicyNumber();
      this.policyType = policyDTO.getPolicyType();
      this.premiumAmount = policyDTO.getPremiumAmount();
      this.coverageAmount = policyDTO.getCoverageAmount();
      this.startDate = policyDTO.getStartDate();
      this.endDate = policyDTO.getEndDate();
   }
   public Policy() {
   }
}