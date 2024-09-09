package com.goalglo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate deadline;
}