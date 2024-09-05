package com.goalglo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for representing a financial goal.
 */
@Data
public class GoalDTO {
  private UUID id;
  private String name;
  private BigDecimal targetAmount;
  private BigDecimal currentAmount;
  private LocalDate deadline;
}