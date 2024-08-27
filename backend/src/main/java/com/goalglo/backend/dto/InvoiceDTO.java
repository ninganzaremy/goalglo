package com.goalglo.backend.dto;

import com.goalglo.backend.entities.InvoiceEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class InvoiceDTO {
   private UUID id;
   private String invoiceNumber;
   private LocalDate issueDate;
   private LocalDate dueDate;
   private BigDecimal amountDue;
   private String status;

   public InvoiceDTO(InvoiceEntity invoiceEntity) {
      this.id = invoiceEntity.getId();
      this.invoiceNumber = invoiceEntity.getInvoiceNumber();
      this.issueDate = invoiceEntity.getIssueDate();
      this.dueDate = invoiceEntity.getDueDate();
      this.amountDue = invoiceEntity.getAmountDue();
      this.status = invoiceEntity.getStatus();
   }
}