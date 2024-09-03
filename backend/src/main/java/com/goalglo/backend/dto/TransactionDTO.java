package com.goalglo.backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for Transaction.
 */
@Data
public class TransactionDTO {
  private UUID id;
  private UUID userId;
  private UUID paymentId;
  private UUID serviceId;
  private BigDecimal amount;
  private String type;
  private String description;
  private LocalDateTime transactionDate;
}