package com.goalglo.dto;

import com.goalglo.entities.Policy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDTO {

   private UUID id;
   private UUID userId;
   private String policyNumber;
   private String policyType;
   private double premiumAmount;
   private double coverageAmount;
   private LocalDate startDate;
   private LocalDate endDate;

   public PolicyDTO(Policy policy) {
      this.id = policy.getId();
      this.userId = policy.getUser().getId();
      this.policyNumber = policy.getPolicyNumber();
      this.policyType = policy.getPolicyType();
      this.premiumAmount = policy.getPremiumAmount();
      this.coverageAmount = policy.getCoverageAmount();
      this.startDate = policy.getStartDate();
      this.endDate = policy.getEndDate();
   }
}