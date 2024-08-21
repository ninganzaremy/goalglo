package com.goalglo.backend.dto;

import com.goalglo.backend.entities.Policy;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class PolicyDTO {
   private UUID id;
   private UUID clientId;
   private String policyNumber;
   private String policyType;
   private BigDecimal premiumAmount;
   private BigDecimal coverageAmount;
   private LocalDate startDate;
   private LocalDate endDate;

   public PolicyDTO(Policy policy) {
      this.id = policy.getId();
      this.clientId = policy.getClient().getId();
      this.policyNumber = policy.getPolicyNumber();
      this.policyType = policy.getPolicyType();
      this.premiumAmount = policy.getPremiumAmount();
      this.coverageAmount = policy.getCoverageAmount();
      this.startDate = policy.getStartDate();
      this.endDate = policy.getEndDate();
   }
}