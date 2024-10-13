package com.goalglo.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for representing account summary information.
 */
@Data
public class AccountSummaryDTO {
  private BigDecimal totalBalance;
  private BigDecimal totalIncome;
  private BigDecimal totalExpenses;
}