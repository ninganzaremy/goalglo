package com.goalglo.dto;

import com.goalglo.entities.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

   private UUID id;
   private UUID userId;
   private UUID serviceId;
   private Long amount;
   private String currency;
   private String status;
   private String paymentMethod;
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;

   public PaymentDTO(Payment payment) {
      this.id = payment.getId();
      this.userId = payment.getUser().getId();
      this.serviceId = payment.getService().getId();
      this.currency = payment.getCurrency();
      this.amount = payment.getAmount();
      this.paymentMethod = payment.getPaymentMethod();

      this.status = payment.getStatus();
      this.createdAt = payment.getCreatedAt();
      this.updatedAt = payment.getUpdatedAt();
   }
}